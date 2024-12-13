package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.ChatMessageEntity;
import org.example.final_project.mapper.ChatMessageMapper;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.repository.IChatRepository;
import org.example.final_project.service.IChatRoomService;
import org.example.final_project.service.IChatService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.final_project.util.specification.ChatMessageSpecification.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatMessageService implements IChatService {
    IChatRepository chatRepository;
    IChatRoomService chatRoomService;
    ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessageDto> getAll() {
        return List.of();
    }

    @Override
    public ChatMessageDto getById(Long id) {
        return chatMessageMapper.toDto(Objects.requireNonNull(chatRepository.findById(id).orElse(null)));
    }

    @Override
    public int save(ChatMessageModel chatMessageModel) {
        var chatRoomId = chatRoomService.getChatRoomId(chatMessageModel.getSenderId(),
                chatMessageModel.getRecipientId(),
                true).orElseThrow(() -> new IllegalArgumentException("Can't find chat room"));
        chatMessageModel.setChatId(chatRoomId);
        chatMessageModel.setSentAt(LocalDateTime.now());
        ChatMessageEntity entity = chatMessageMapper.toEntity(chatMessageModel);
        chatRepository.save(entity);
        chatMessageModel.setId(entity.getId());
        return 1;
    }


    @Override
    public int update(Long aLong, ChatMessageModel chatMessageModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<ChatMessageDto> getChatMessages(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(id -> getByChatId(id, senderId, recipientId)).orElse(new ArrayList<>());
    }

    @Override
    public List<ChatMessageDto> getByChatId(String chatId, Long senderId, Long recipientId) {
        return chatRepository.findAll(
                        Specification.where(hasChatId(chatId))
                                .and(hasSenderId(senderId))
                                .and(hasRecipientId(recipientId))
                ).stream()
                .map(chatMessageMapper::toDto)
                .toList();
    }
}
