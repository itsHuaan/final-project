package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.entity.ChatRoomEntity;
import org.example.final_project.mapper.ChatRoomMapper;
import org.example.final_project.repository.IChatRoomRepository;
import org.example.final_project.service.IChatRoomService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.final_project.util.specification.ChatRoomSpecification.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatRoomService implements IChatRoomService {

    IChatRoomRepository chatRoomRepository;
    ChatRoomMapper chatRoomMapper;

    @Override
    public Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean isExist) {
        Long firstId = Math.min(senderId, recipientId);
        Long secondId = Math.max(senderId, recipientId);
        return chatRoomRepository.findOne(Specification.where(
                        hasNormalizedChat(firstId, secondId)
                ))
                .map(ChatRoomEntity::getChatId)
                .or(() -> {
                    if (isExist) {
                        var chatId = createChatId(firstId, secondId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    @Override
    public ChatRoomDto getChatRoom(Long senderId, Long recipientId) {
        return chatRoomMapper.toDto(Objects.requireNonNull(chatRoomRepository.findOne(Specification.where(hasSenderId(senderId).and(hasRecipientId(recipientId)))).orElse(null)));
    }

    private String createChatId(Long senderId, Long recipientId) {
        Long firstId = Math.min(senderId, recipientId);
        Long secondId = Math.max(senderId, recipientId);
        var chatId = String.format("%s_%s", firstId, secondId);
        LocalDateTime now = LocalDateTime.now();
        ChatRoomEntity senderRecipient = ChatRoomEntity.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .lastUpdatedAt(now)
                .build();
        ChatRoomEntity recipientSender = ChatRoomEntity.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .lastUpdatedAt(now)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}

