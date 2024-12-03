package org.example.final_project.service;

import org.example.final_project.dto.ChatMessageDto;
import org.example.final_project.model.ChatMessageModel;

import java.util.List;

public interface IChatService extends IBaseService<ChatMessageDto, ChatMessageModel, Long> {
    List<ChatMessageDto> getChatMessages(Long senderId, Long recipientId);

    List<ChatMessageDto> getByChatId(String chatId);
}
