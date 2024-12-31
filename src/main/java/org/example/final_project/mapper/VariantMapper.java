package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantMapper {
    ProductMapper productMapper;
    IPromotionService promotionService;
    IImageProductRepository imageProductRepository;
    ProductOptionMapper productOptionMapper;

    public CartSkuDto toDto(SKUEntity entity) {
        double discountedPrice = promotionService.findAllPromotionByNow(entity.getProduct().getId()) != null
                ? entity.getPrice() * ((100 - promotionService.findAllPromotionByNow(entity.getProduct().getId()).getDiscountPercentage()) / 100)
                : entity.getPrice();
        String image = imageProductRepository.findAllByProductEntity_Id(entity.getProduct().getId()).get(0).getImageLink();
        return CartSkuDto.builder()
                .itemId(entity.getId())
                .option1(entity.getOption1() != null
                        ? productOptionMapper.toProductOptionDto(entity.getValue1())
                        : null)
                .option2(entity.getOption2() != null
                        ? productOptionMapper.toProductOptionDto(entity.getValue2())
                        : null)
                .productFamily(productMapper.toProductFamilyDto(entity.getProduct()))
                .price(entity.getPrice())
                .discountedPrice(discountedPrice)
                .quantity(entity.getQuantity())
                .image(!entity.getImage().isEmpty()
                        ? entity.getImage()
                        : image)
                .build();
    }
}
