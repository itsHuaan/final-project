package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.entity.ChatRoomEntity;
import org.example.final_project.mapper.ChatRoomMapper;
import org.example.final_project.repository.IChatRoomRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IChatRoomService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.example.final_project.specification.ChatRoomSpecification.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatRoomService implements IChatRoomService {

    IChatRoomRepository chatRoomRepository;
    ChatRoomMapper chatRoomMapper;
    IUserRepository userRepository;

    @Override
    public Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createIfNotExist) {
        Long firstId = Math.min(senderId, recipientId);
        Long secondId = Math.max(senderId, recipientId);
        return chatRoomRepository.findOne(Specification.where(
                        hasNormalizedChat(firstId, secondId)
                ))
                .map(ChatRoomEntity::getChatId)
                .or(() -> {
                    if (createIfNotExist) {
                        try {
                            var chatId = createChatId(firstId, secondId);
                            return Optional.of(chatId);
                        } catch (NoSuchElementException e) {
                            log.error(e.getMessage());
                        }
                    }
                    return Optional.empty();
                });
    }

    @Override
    public ChatRoomDto getChatRoom(Long senderId, Long recipientId) {
        return chatRoomMapper.toDto(Objects.requireNonNull(chatRoomRepository.findOne(Specification.where(hasSenderId(senderId).and(hasRecipientId(recipientId)))).orElse(null)));
    }

    @Override
    public List<ChatUserDto> getChatUsers(Long senderId) {
        return chatRoomRepository.findBySenderId(senderId).stream()
                .map(chatRoomMapper::toChatUserDto)
                .sorted(Comparator.comparing((ChatUserDto chatUserDto) ->
                                Optional.ofNullable(chatUserDto.getLastMessage())
                                        .map(ChatMessageDto::getSentAt)
                                        .orElse(LocalDateTime.MIN))
                        .reversed())

                .toList();
    }

    private String createChatId(Long senderId, Long recipientId) {
        Long _senderId = userRepository.findById(senderId).orElseThrow(
                () -> new NoSuchElementException("Sender not found")
        ).getUserId();
        Long _recipientId = userRepository.findById(recipientId).orElseThrow(
                () -> new NoSuchElementException("Recipient not found")
        ).getUserId();
        var chatId = String.format("%s_%s", Math.min(_senderId, _recipientId), Math.max(_senderId, _recipientId));
        LocalDateTime now = LocalDateTime.now();
        ChatRoomEntity senderRecipient = ChatRoomEntity.builder()
                .chatId(chatId)
                .senderId(_senderId)
                .recipientId(_recipientId)
                .lastUpdatedAt(now)
                .build();
        ChatRoomEntity recipientSender = ChatRoomEntity.builder()
                .chatId(chatId)
                .senderId(_recipientId)
                .recipientId(_senderId)
                .lastUpdatedAt(now)
                .build();
        chatRoomRepository.saveAll(List.of(senderRecipient, recipientSender));
        return chatId;
    }
}

