package org.example.final_project.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.SKUModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SKUMapper {
    public SKUDto convertToDto(SKUEntity entity){
        return SKUDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
    public SKUEntity convertToEntity(SKUModel model){
        return SKUEntity.builder()
                .price(model.getPrice())
                .quantity(model.getQuantity())
                .build();
    }
}
