package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartSkuMapper {
    IProductOptionValueService valueService;
    CartProductMapper cartProductMapper;
    public CartSkuDto convertToDto(SKUEntity entity){
        return CartSkuDto.builder()
                .id(entity.getId())
                .value1(valueService.getById(entity.getValue1().getId()))
                .value2(valueService.getById(entity.getValue2().getId()))
                .cartProductDto(cartProductMapper.toDto(entity.getProduct()))
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
}
