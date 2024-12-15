package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ChatHistoryDto;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.*;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageMapper {
    IUserRepository userRepository;
    public ChatMessageEntity toEntity(ChatMessageModel chatMessageModel) {
        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .chatId(chatMessageModel.getChatId())
                .senderId(chatMessageModel.getSenderId())
                .recipientId(chatMessageModel.getRecipientId())
                .message(chatMessageModel.getMessage())
                .sentAt(chatMessageModel.getSentAt())
                .build();

        if (chatMessageModel.getMediaUrls() != null) {
            List<ChatMessageMediaEntity> mediaEntities = chatMessageModel.getMediaUrls().stream()
                    .map(url -> ChatMessageMediaEntity.builder()
                            .mediaUrl(url)
                            .chatMessage(chatMessageEntity)
                            .build())
                    .toList();
            chatMessageEntity.setChatMedias(mediaEntities);
        }

        return chatMessageEntity;
    }

    public ChatMessageDto toDto(ChatMessageEntity chatMessageEntity) {
        return ChatMessageDto.builder()
                .messageId(chatMessageEntity.getId())
                .chatId(chatMessageEntity.getChatId())
                .senderId(chatMessageEntity.getSenderId())
                .recipientId(chatMessageEntity.getRecipientId())
                .message(chatMessageEntity.getMessage())
                .mediaUrls(chatMessageEntity.getChatMedias().stream()
                        .map(ChatMessageMediaEntity::getMediaUrl)
                        .toList())
                .sentAt(chatMessageEntity.getSentAt())
                .build();
    }
    public ChatHistoryDto toChatHistoryDto(ChatMessageEntity chatMessageEntity) {
        return ChatHistoryDto.builder()
                .sender(userRepository.findById(chatMessageEntity.getSenderId()).orElseThrow(
                        () -> new RuntimeException("Sender does not exist")
                ).getUsername())
                .profilePicture(userRepository.findById(chatMessageEntity.getSenderId()).orElseThrow(
                        () -> new RuntimeException("Sender does not exist")
                ).getProfilePicture())
                .message(chatMessageEntity.getMessage())
                .sentAt(chatMessageEntity.getSentAt())
                .build();
    }
}
