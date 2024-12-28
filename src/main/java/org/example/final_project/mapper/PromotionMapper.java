package org.example.final_project.mapper;

import org.example.final_project.dto.PromotionDto;
import org.example.final_project.entity.PromotionEntity;
import org.example.final_project.model.PromotionModel;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionDto convertToDto(PromotionEntity promotionEntity) {
        return PromotionDto.builder()
                .id(promotionEntity.getId())
                .name(promotionEntity.getName())
                .discountPercentage(promotionEntity.getDiscountPercentage())
                .startDate(promotionEntity.getStartDate())
                .endDate(promotionEntity.getEndDate())
                .build();
    }

    public PromotionEntity convertToEntity(PromotionModel model) {
        return PromotionEntity.builder()
                .discountPercentage(model.getDiscountPercentage())
                .name(model.getName())
                .startDate(model.getStartDate())
                .endDate(model.getEndDate())
                .build();
    }
}
