package org.example.final_project.mapper;

import org.example.final_project.dto.ProductOptionValuesDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.model.ProductOptionsValueModel;
import org.springframework.stereotype.Component;

@Component
public class ProductOptionValuesMapper {
    public ProductOptionValuesDto convertToDto(ProductOptionValuesEntity entity){
        return ProductOptionValuesDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
    public ProductOptionValuesEntity convertToEntity(ProductOptionsValueModel model){
        return ProductOptionValuesEntity.builder()
                .name(model.getName())
                .build();
    }
}
