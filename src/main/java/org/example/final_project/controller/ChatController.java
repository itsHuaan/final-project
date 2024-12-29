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
import org.example.final_project.service.impl.ChatMessageService;
import org.example.final_project.service.impl.ChatRoomService;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
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
        Page<ChatMessageDto> chatMessages;
        String apiMessage;
        HttpStatus status;

        try {
            chatMessages = chatMessageService.getChatMessages(senderId, recipientId, pageable);
            apiMessage = !chatMessages.isEmpty() ? "Messages fetched" : "No messages fetched";
            status = HttpStatus.OK;
        } catch (IllegalArgumentException e){
            chatMessages = null;
            apiMessage = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(
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
        HttpStatus status = result != 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        String message = result != 0 ? "Deleted" : "Failed to delete";
        return ResponseEntity.status(status).body(
                createResponse(
                        status,
                        message,
                        null
                ));
    }

    @Operation(summary = "Retrieve all recipients who have chatted with the sender")
    @GetMapping("/{senderId}")
    public ResponseEntity<?> findChatUsers(@PathVariable long senderId) {
        List<ChatUserDto> chatUsers;
        HttpStatus status;
        String message;
        try {
            chatUsers = chatRoomService.getChatUsers(senderId);
            status = !chatUsers.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
            message = !chatUsers.isEmpty() ? "Chat users fetched" : "No chat users fetched";
        } catch (Exception e) {
            chatUsers = null;
            status = HttpStatus.BAD_REQUEST;
            message = e.getMessage();
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        chatUsers
                )
        );
    }
}
