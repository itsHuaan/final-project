package org.example.final_project.mapper;

import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.entity.ChatRoomEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {
    public ChatRoomDto toDto(ChatRoomEntity chatRoomEntity) {
        return ChatRoomDto.builder()
                .id(chatRoomEntity.getId())
                .chatId(chatRoomEntity.getChatId())
                .senderId(chatRoomEntity.getSenderId())
                .recipientId(chatRoomEntity.getRecipientId())
                .build();
    }
}
