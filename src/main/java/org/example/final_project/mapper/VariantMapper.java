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

    public CartSkuDto toDto(SKUEntity entity) {
        return CartSkuDto.builder()
                .itemId(entity.getId())
                .value1(entity.getOption1() != null
                        ? valueService.getById(entity.getValue1().getId())
                        : null)
                .value2(entity.getOption2() != null
                        ? valueService.getById(entity.getValue2().getId())
                        : null)
                .productFamily(productMapper.toProductFamilyDto(entity.getProduct()))
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
}
