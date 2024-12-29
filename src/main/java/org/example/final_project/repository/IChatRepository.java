package org.example.final_project.repository;

import org.example.final_project.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IChatRepository extends JpaRepository<ChatMessageEntity, Long>, JpaSpecificationExecutor<ChatMessageEntity> {
    ChatMessageEntity findTopByChatIdOrderBySentAtDesc(String chatId);

    Page<ChatMessageEntity> findAllByChatIdOrderBySentAtDesc(String chatId, Pageable pageable);
}
