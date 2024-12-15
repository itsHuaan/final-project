package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.entity.ChatMessageEntity;
import org.example.final_project.entity.ChatMessageMediaEntity;
import org.example.final_project.mapper.ChatMessageMapper;
import org.example.final_project.mapper.ChatRoomMapper;
import org.example.final_project.model.ChatMessageModel;
import org.example.final_project.repository.IChatMessageMediaRepository;
import org.example.final_project.repository.IChatRepository;
import org.example.final_project.service.IChatRoomService;
import org.example.final_project.service.IChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.final_project.util.specification.ChatMessageSpecification.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatMessageService implements IChatMessageService {
    IChatRepository chatRepository;
    IChatRoomService chatRoomService;
    ChatMessageMapper chatMessageMapper;
    ChatRoomMapper chatRoomMapper;
    MediaUploadService mediaUploadService;
    IChatMessageMediaRepository chatMessageMediaRepository;

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
    public Page<ChatMessageDto> getChatMessages(Long senderId, Long recipientId, Pageable pageable) {
        return chatRoomService.getChatRoomId(senderId, recipientId, false)
                .map(chatId -> chatRepository.findAll(Specification.where(hasChatId(chatId)), pageable)
                        .map(chatMessageMapper::toDto))
                .orElse(Page.empty(pageable));
    }

    @Override
    public List<String> saveMediaFiles(Long messageId, List<MultipartFile> mediaFiles) {
        List<String> mediaUrls = new ArrayList<>();
        try {
            ChatMessageEntity chatMessage = chatRepository.findById(messageId)
                    .orElseThrow(() -> new IllegalArgumentException("Message ID not found: " + messageId));
            for (MultipartFile file : mediaFiles) {
                String mediaUrl = mediaUploadService.uploadOneImage(file);
                mediaUrls.add(mediaUrl);

                ChatMessageMediaEntity mediaEntity = ChatMessageMediaEntity.builder()
                        .mediaUrl(mediaUrl)
                        .chatMessage(chatMessage)
                        .build();
                chatMessage.getChatMedias().add(mediaEntity);
                chatMessageMediaRepository.save(mediaEntity);
            }
            chatRepository.save(chatMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mediaUrls;
    }
}
