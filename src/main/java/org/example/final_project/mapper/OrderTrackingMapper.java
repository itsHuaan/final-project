package org.example.final_project.mapper;

import org.example.final_project.dto.OrderTrackingDto;
import org.example.final_project.entity.OrderTrackingEntity;

public class OrderTrackingMapper {
    public static OrderTrackingDto toOrderTrackingDto(OrderTrackingEntity orderTrackingEntity) {
        return OrderTrackingDto.builder()
                .id(orderTrackingEntity.getId())
                .note(orderTrackingEntity.getNote())
                .status(orderTrackingEntity.getStatus())
                .paidDate(orderTrackingEntity.getPaidDate())
                .createdAt(orderTrackingEntity.getCreatedAt())
                .shopId(orderTrackingEntity.getShopId())
                .build();
    }
}
