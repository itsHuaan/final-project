package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantMapper {
    IProductOptionValueRepository valueRepository;
    ProductOptionValueMapper valueMapper;
    ProductMapper productMapper;

    public CartSkuDto toDto(SKUEntity entity) {
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
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
}
