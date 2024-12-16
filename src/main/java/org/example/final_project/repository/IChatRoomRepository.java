package org.example.final_project.repository;

import org.example.final_project.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, JpaSpecificationExecutor<ChatRoomEntity> {
    Optional<ChatRoomEntity> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    List<ChatRoomEntity> findBySenderId(Long senderId);
}
