package org.example.final_project.mapper;

import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.model.ProductOptionValueModel;
import org.springframework.stereotype.Component;

@Component
public class ProductOptionValueMapper {
    public ProductOptionValueDto convertToDto(ProductOptionValuesEntity entity){
        return ProductOptionValueDto.builder()
                .valueId(entity.getId())
                .name(entity.getName())
                .build();
    }
    public ProductOptionValuesEntity convertToEntity(ProductOptionValueModel model){
        return ProductOptionValuesEntity.builder()
                .name(model.getName())
                .build();
    }
}
