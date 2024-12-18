package org.example.final_project.mapper;

import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.model.NotificationModel;

public class NotificationMapper {
    public static NotificationEntity toEntity(NotificationModel notifiModel) {
        return NotificationEntity.builder()
                .title(notifiModel.getTitle())
                .content(notifiModel.getContent())
                .createdAt(notifiModel.getCreatedAt())
                .senderId(notifiModel.getSenderId())
                .recipientId(notifiModel.getRecipientId())
                .build();
    }
}
