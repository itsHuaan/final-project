package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantMapper {
    IProductOptionValueRepository valueRepository;
    ProductOptionValueMapper valueMapper;
    ProductMapper productMapper;
    IPromotionService promotionService;

    public CartSkuDto toDto(SKUEntity entity) {
        double discountedPrice = promotionService.findAllPromotionByNow(entity.getProduct().getId()) != null
                ? entity.getPrice() * ((100 - promotionService.findAllPromotionByNow(entity.getProduct().getId()).getDiscountPercentage()) / 100)
                : entity.getPrice();
        return CartSkuDto.builder()
                .itemId(entity.getId())
                .value1(entity.getOption1() != null
                        ? valueMapper.convertToDto(Objects.requireNonNull(valueRepository.findById(entity.getValue1().getId()).orElse(null)))
                        : null)
                .value2(entity.getOption2() != null
                        ? valueMapper.convertToDto(Objects.requireNonNull(valueRepository.findById(entity.getValue2().getId()).orElse(null)))
                        : null)
                .productFamily(productMapper.toProductFamilyDto(entity.getProduct()))
                .price(entity.getPrice())
                .discountedPrice(discountedPrice)
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
}
