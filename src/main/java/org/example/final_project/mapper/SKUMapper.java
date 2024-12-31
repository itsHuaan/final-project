package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.SKUModel;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SKUMapper {
    ProductOptionMapper optionMapper;
    IPromotionService promotionService;
    IImageProductRepository imageProductRepository;

    public SKUDto convertToDto(SKUEntity entity) {
        String productImage = imageProductRepository.findAllByProductEntity_Id(entity.getProduct().getId()).get(0).getImageLink();
        String skuImage = entity.getImage();
        return SKUDto.builder()
                .productId(entity.getProduct().getId())
                .variantId(entity.getId())
                .option1(entity.getOption1() != null
                        ? optionMapper.toProductOptionDto(entity.getValue1())
                        : null)
                .option2(entity.getOption2() != null
                        ? optionMapper.toProductOptionDto(entity.getValue2())
                        : null)
                .oldPrice(entity.getPrice())
                .newPrice(promotionService.findAllPromotionByNow(entity.getProduct().getId()) != null
                        ? entity.getPrice() * ((100 - promotionService.findAllPromotionByNow(entity.getProduct().getId()).getDiscountPercentage()) / 100)
                        : entity.getPrice())
                .quantity(entity.getQuantity())
                .image(skuImage != null
                        ? skuImage.isEmpty() ? productImage : entity.getImage()
                        : entity.getImage())
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
