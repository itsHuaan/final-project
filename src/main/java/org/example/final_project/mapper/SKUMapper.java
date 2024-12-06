package org.example.final_project.mapper;

import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.SKUModel;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SKUMapper {
    @Autowired
    IProductOptionValueService valueService;
    @Autowired
    IProductOptionService optionService;

    @Autowired
    CartProductMapper cartProductMapper;

    public SKUDto convertToDto(SKUEntity entity) {
        Long optionId1 = entity.getOption1().getId();
        Long optionId2 = entity.getOption2().getId();
        Long value1 = entity.getValue1().getId();
        Long value2 = entity.getValue2().getId();
        return SKUDto.builder()
                .id(entity.getId())
                .option1(optionId1 != null
                        ? optionService.getById(entity.getOption1().getId())
                        : null)
                .value1(value1 != null
                        ? valueService.getById(entity.getValue1().getId())
                        : null)
                .option2(optionId2 != null
                        ? optionService.getById(entity.getOption2().getId())
                        : null)
                .value2(value2 != null
                        ? valueService.getById(entity.getValue2().getId())
                        : null)
                .cartProductDto(cartProductMapper.toDto(entity.getProduct()))
                .shopId(entity.getProduct().getUser().getUserId())
                .shopName(entity.getProduct().getUser().getShop_name())
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
                .image(model.getImage())
                .build();
    }
}
