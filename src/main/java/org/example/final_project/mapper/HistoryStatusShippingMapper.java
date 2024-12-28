package org.example.final_project.mapper;

import org.example.final_project.dto.HistoryStatusShippingDto;
import org.example.final_project.entity.HistoryStatusShippingEntity;

public class HistoryStatusShippingMapper {
    public static HistoryStatusShippingDto toDTO(HistoryStatusShippingEntity entity) {
        return HistoryStatusShippingDto.builder()
                .id(entity.getId())
                .createdChangeStatus(entity.getCreatedChangeStatus())
                .status(entity.getStatus())
                .build();
    }
}
