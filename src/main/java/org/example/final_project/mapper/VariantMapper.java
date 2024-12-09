package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantMapper {
    IProductOptionValueService valueService;
    ProductMapper productMapper;
    public CartSkuDto toDto(SKUEntity entity){
        return CartSkuDto.builder()
                .itemId(entity.getId())
                .value1(valueService.getById(entity.getValue1().getId()))
                .value2(valueService.getById(entity.getValue2().getId()))
                .productFamily(productMapper.toProductFamilyDto(entity.getProduct()))
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
}
