package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ChatHistoryDto;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.ChatMessageEntity;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageMapper {
    UserMapper userMapper;
    IUserRepository userRepository;
    public ChatMessageDto toDto(ChatMessageEntity chatMessageEntity) {
        return ChatMessageDto.builder()
                .messageId(chatMessageEntity.getId())
                .chatId(chatMessageEntity.getChatId())
                .senderId(chatMessageEntity.getSenderId())
                .senderUsername(userRepository.findById(chatMessageEntity.getSenderId()).orElseThrow(
                        () -> new RuntimeException("Sender does not exist")
                ).getUsername())
                .recipientId(chatMessageEntity.getRecipientId())
                .recipientUsername(userRepository.findById(chatMessageEntity.getRecipientId()).orElseThrow(
                        () -> new RuntimeException("Recipient does not exist")
                ).getUsername())
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
