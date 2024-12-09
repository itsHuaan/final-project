package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.SKUModel;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SKUMapper {
    ProductOptionMapper optionMapper;

    public SKUDto convertToDto(SKUEntity entity) {
        return SKUDto.builder()
                .variantId(entity.getId())
                .option1(optionMapper.convertToDtoWithValue(entity.getOption1(), entity.getValue1()))
                .option2(optionMapper.convertToDtoWithValue(entity.getOption2(), entity.getValue2()))
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }

    public SKUEntity convertToEntity(SKUModel model) {
        return SKUEntity.builder()
                .id(model.getId())
                .price(model.getPrice())
                .quantity(model.getQuantity())
                .build();
    }

}
