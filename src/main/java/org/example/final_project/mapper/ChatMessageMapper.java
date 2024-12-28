package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ChatHistoryDto;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.ChatMessageEntity;
import org.example.final_project.entity.ChatMessageMediaEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

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
                .isSeen(0)
                .build();

        if (chatMessageModel.getMediaUrls() != null) {
            List<ChatMessageMediaEntity> mediaEntities = chatMessageModel.getMediaUrls().stream()
                    .map(url -> ChatMessageMediaEntity.builder()
                            .mediaUrl(url)
                            .chatMessage(chatMessageEntity)
                            .build())
                    .toList();
            chatMessageEntity.setChatMedias(mediaEntities);
        } else {
            chatMessageEntity.setChatMedias(null);
        }

        return chatMessageEntity;
    }

    public ChatMessageDto toDto(ChatMessageEntity chatMessageEntity) {
        UserEntity sender = userRepository.findById(chatMessageEntity.getSenderId()).orElseThrow(
                () -> new NoSuchElementException("Sender not found")
        );
        UserEntity recipient = userRepository.findById(chatMessageEntity.getRecipientId()).orElseThrow(
                () -> new NoSuchElementException("Recipient not found")
        );

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
                .senderName(sender.getUsername())
                .recipientName(recipient.getUsername())
                .build();
    }

    public ChatHistoryDto toChatHistoryDto(ChatMessageEntity chatMessageEntity) {
        UserEntity sender = userRepository.findById(chatMessageEntity.getSenderId()).orElseThrow(
                () -> new RuntimeException("Sender does not exist")
        );
        return ChatHistoryDto.builder()
                .sender(sender.getShop_status() == 1 ? sender.getShop_name() : sender.getName())
                .profilePicture(sender.getProfilePicture())
                .message(chatMessageEntity.getMessage())
                .sentAt(chatMessageEntity.getSentAt())
                .build();
    }
}
