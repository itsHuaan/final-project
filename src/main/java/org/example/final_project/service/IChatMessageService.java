package org.example.final_project.service;

import org.example.final_project.dto.ChatHistoryDto;
import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.model.ChatMessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatMessageService extends IBaseService<ChatMessageDto, ChatMessageModel, Long> {
    Page<ChatMessageDto> getChatMessages(Long senderId, Long recipientId, Pageable pageable);
}
