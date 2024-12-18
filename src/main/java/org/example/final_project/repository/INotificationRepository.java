package org.example.final_project.repository;

import org.example.final_project.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface INotificationRepository extends JpaRepository<NotificationEntity, Long> {

}
