package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.entity.ChatRoomEntity;
import org.example.final_project.repository.IChatRepository;
import org.example.final_project.repository.IUserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.example.final_project.util.specification.ChatMessageSpecification.hasChatId;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomMapper {
    IUserRepository userRepository;
    ChatMessageMapper chatMessageMapper;
    IChatRepository chatRepository;

    public ChatRoomDto toDto(ChatRoomEntity chatRoomEntity) {
        return ChatRoomDto.builder()
                .chatId(chatRoomEntity.getChatId())
                .sender(userRepository.findById(chatRoomEntity.getSenderId()).orElseThrow(
                        () -> new RuntimeException("Sender not found")
                ).getUsername())
                .recipient(userRepository.findById(chatRoomEntity.getRecipientId()).orElseThrow(
                        () -> new RuntimeException("Sender not found")
                ).getUsername())
                .messages(chatRepository.findAll(
                                Specification.where(hasChatId(String.format("%s_%s", chatRoomEntity.getSenderId(), chatRoomEntity.getRecipientId()))))
                        .stream()
                        .map(chatMessageMapper::toChatHistoryDto)
                        .toList())
                .build();
    }
}
