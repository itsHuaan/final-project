package org.example.final_project.repository;

import org.example.final_project.entity.ChatMessageMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IChatMessageMediaRepository extends JpaRepository<ChatMessageMediaEntity, Long>, JpaSpecificationExecutor<ChatMessageMediaEntity> {
}
