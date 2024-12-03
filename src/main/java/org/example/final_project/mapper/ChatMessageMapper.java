package org.example.final_project.mapper;

import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.ChatMessageEntity;
import org.example.final_project.model.ChatMessageModel;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {
    public ChatMessageDto toDto(ChatMessageEntity chatMessageEntity) {
        return ChatMessageDto.builder()
                .id(chatMessageEntity.getId())
                .chatId(chatMessageEntity.getChatId())
                .senderId(chatMessageEntity.getSenderId())
                .recipientId(chatMessageEntity.getRecipientId())
                .message(chatMessageEntity.getMessage())
                .sentAt(chatMessageEntity.getSentAt())
                .build();
    }

    public ChatMessageEntity toEntity(ChatMessageModel chatMessageModel) {
        return ChatMessageEntity.builder()
                .chatId(chatMessageModel.getChatId())
                .senderId(chatMessageModel.getSenderId())
                .recipientId(chatMessageModel.getRecipientId())
                .message(chatMessageModel.getMessage())
                .sentAt(chatMessageModel.getSentAt())
                .build();
    }
}
