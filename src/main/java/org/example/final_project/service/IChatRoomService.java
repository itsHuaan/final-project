package org.example.final_project.service;

import org.example.final_project.dto.ChatRoomDto;
import org.example.final_project.dto.ChatUserDto;

import java.util.List;
import java.util.Optional;

public interface IChatRoomService {
    Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createIfNotExist);

    ChatRoomDto getChatRoom(Long senderId, Long recipientId);

    List<ChatUserDto> getChatUsers(Long senderId);
}
