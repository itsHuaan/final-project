package org.example.final_project.mapper;

import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.NotificationDto;
import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.repository.IOrderRepository;

@RequiredArgsConstructor

public class NotificationMapper {
    private final IOrderRepository orderRepository;


    public static NotificationEntity toEntity(NotificationModel notifiModel) {
        return NotificationEntity.builder()
                .title(notifiModel.getTitle())
                .content(notifiModel.getContent())
                .adminId(notifiModel.getAdminId())
                .userId(notifiModel.getUserId())
                .image(notifiModel.getImage())
                .shopId(notifiModel.getShopId())
                .orderCode(notifiModel.getOrderCode())
                .build();
    }

    public static NotificationDto toNotificationDto(NotificationEntity notifiEntity) {

        return NotificationDto.builder()
                .id(notifiEntity.getId())
                .title(notifiEntity.getTitle())
                .content(notifiEntity.getContent())
                .createdAt(notifiEntity.getCreatedAt())
                .adminId(notifiEntity.getAdminId())
                .userId(notifiEntity.getUserId())
                .isRead(notifiEntity.getIsRead())
                .shopUserId(notifiEntity.getShopUserId())
                .orderId(notifiEntity.getOrderId())
                .orderCode(notifiEntity.getOrderCode())
                .image(notifiEntity.getImage())
                .shopId(notifiEntity.getShopId())
                .orderCode(notifiEntity.getOrderCode())
                .build();
    }
}
