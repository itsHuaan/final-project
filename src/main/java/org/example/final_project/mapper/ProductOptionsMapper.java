package org.example.final_project.mapper;

import org.example.final_project.dto.ProductOptionsDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;
import org.springframework.stereotype.Component;

@Component
public class ProductOptionsMapper {
    public ProductOptionsDto convertToDto(ProductOptionsEntity entity){
        return ProductOptionsDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
    public ProductOptionsEntity convertToEntity(ProductOptionsModel model){
        return ProductOptionsEntity.builder()
                .name(model.getName())
                .build();
    }
}
