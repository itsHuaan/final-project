package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.model.ChatNotificationModel;
import org.example.final_project.model.validation.PageableValidation;
import org.example.final_project.service.impl.ChatMessageService;
import org.example.final_project.service.impl.ChatRoomService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/message")
@Tag(name = "Chat Controller")
public class ChatController {
    SimpMessagingTemplate messagingTemplate;
    ChatMessageService chatMessageService;
    ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageModel chatMessageModel) {
        try {
            chatMessageService.save(chatMessageModel);
            ChatMessageDto chatMessageDto = chatMessageService.getById(chatMessageModel.getId());
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chatMessageDto.getRecipientId()),
                    "/queue/messages",
                    ChatNotificationModel.builder()
                            .id(chatMessageDto.getMessageId())
                            .senderId(chatMessageDto.getSenderId())
                            .recipientId(chatMessageDto.getRecipientId())
                            .message(chatMessageDto.getMessage())
                            .mediaUrls(chatMessageDto.getMediaUrls())
                            .sentAt(chatMessageDto.getSentAt())
                            .build()
            );
        } catch (IllegalArgumentException e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }

    @Operation(summary = "Retrieve chat history by sender ID and recipient Id")
    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessage(@PathVariable long senderId,
                                             @PathVariable long recipientId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageableValidation.setDefault(size, page);
        Page<ChatMessageDto> chatMessages = chatMessageService.getChatMessages(senderId, recipientId, pageable);
        String apiMessage = !chatMessages.isEmpty() ? "Messages fetched" : "No messages fetched";
        HttpStatus status = !chatMessages.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        apiMessage,
                        chatMessages
                )
        );
    }

    @Operation(summary = "Delete all message between 2 users")
    @DeleteMapping("/{senderId}/{recipientId}")
    public ResponseEntity<?> deleteChatMessages(@PathVariable long senderId,
                                                @PathVariable long recipientId) {
        int result = chatMessageService.deleteChat(senderId, recipientId);
        return result != 0
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Deleted",
                        null
                ))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Failed to delete",
                        null
                ));
    }

    @Operation(summary = "Retrieve all recipients who have chatted with the sender")
    @GetMapping("/{senderId}")
    public ResponseEntity<?> findChatUsers(@PathVariable long senderId) {
        List<ChatUserDto> chatUsers = chatRoomService.getChatUsers(senderId);
        return !chatUsers.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Fetched",
                        chatUsers
                )
        )
                : ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.NO_CONTENT,
                        "No users fetched",
                        chatUsers
                )
        );
    }
}
