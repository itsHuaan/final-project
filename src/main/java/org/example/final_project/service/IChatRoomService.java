package org.example.final_project.service;

import java.util.Optional;

public interface IChatRoomService {
    Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean isExist);
}
