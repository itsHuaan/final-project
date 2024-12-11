package org.example.final_project.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.model.ChatNotification;
import org.example.final_project.service.impl.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    SimpMessagingTemplate messagingTemplate;
    ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageModel chatMessageModel) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        try {
            chatMessageService.save(chatMessageModel);
            chatMessageDto = chatMessageService.getById(chatMessageModel.getId());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        messagingTemplate.convertAndSendToUser(String.valueOf(chatMessageModel.getRecipientId()),
                "/queue/messages",
                ChatNotification.builder()
                        .id(chatMessageDto.getId())
                        .senderId(chatMessageDto.getSenderId())
                        .recipientId(chatMessageDto.getRecipientId())
                        .content(chatMessageDto.getMessage())
                        .build());
    }

    @GetMapping("/message/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessage(@PathVariable long recipientId, @PathVariable long senderId){
        return ResponseEntity.ok(chatMessageService.getChatMessages(recipientId, senderId));
    }
}
