package org.example.final_project.repository;

import org.example.final_project.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;


public interface INotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n from NotificationEntity n where n.recipientId = :recipientId")
    List<NotificationEntity> findByRecipientId(@Param("recipientId") Long recipientId);

}
