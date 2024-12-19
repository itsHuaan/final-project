package org.example.final_project.mapper;

import org.example.final_project.dto.NotificationDto;
import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.model.NotificationModel;

public class NotificationMapper {
    public static NotificationEntity toEntity(NotificationModel notifiModel) {
        return NotificationEntity.builder()
                .title(notifiModel.getTitle())
                .content(notifiModel.getContent())
                .senderId(notifiModel.getSenderId())
                .recipientId(notifiModel.getRecipientId())
                .image(notifiModel.getImage())
                .build();
    }

    public static NotificationDto toNotificationDto(NotificationEntity notifiEntity) {
        return NotificationDto.builder()
                .id(notifiEntity.getId())
                .title(notifiEntity.getTitle())
                .content(notifiEntity.getContent())
                .createdAt(notifiEntity.getCreatedAt())
                .senderId(notifiEntity.getSenderId())
                .recipientId(notifiEntity.getRecipientId())
                .isRead(notifiEntity.getIsRead())
                .image(notifiEntity.getImage())
                .build();
    }
}
