package org.example.final_project.mapper;

import org.example.final_project.dto.HistoryStatusShippingDto;
import org.example.final_project.dto.OrderTrackingDto;
import org.example.final_project.entity.OrderTrackingEntity;

import java.util.List;

public class OrderTrackingMapper {
    public static OrderTrackingDto toOrderTrackingDto(OrderTrackingEntity orderTrackingEntity) {
        List<HistoryStatusShippingDto> historyStatusShippingDtoList = orderTrackingEntity.getHistoryStatusShippingEntities().stream().map(HistoryStatusShippingMapper::toDTO).toList();
        return OrderTrackingDto.builder()
                .id(orderTrackingEntity.getId())
                .note(orderTrackingEntity.getNote())
                .status(orderTrackingEntity.getStatus())
                .paidDate(orderTrackingEntity.getPaidDate())
                .createdAt(orderTrackingEntity.getCreatedAt())
                .shopId(orderTrackingEntity.getShopId())
                .historyStatusShippingDtoList(historyStatusShippingDtoList)
                .build();
    }
}
