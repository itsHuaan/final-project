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
    public SKUDto convertToDto(SKUEntity entity){
        return SKUDto.builder()
                .id(entity.getId())
                .option1(optionService.getById(entity.getOption1().getId()))
                .value1(valueService.getById(entity.getValue1().getId()))
                .option2(optionService.getById(entity.getOption2().getId()))
                .value2(valueService.getById(entity.getValue2().getId()))
                .productName(entity.getProduct().getName())
                .shopId(entity.getProduct().getUser().getUserId())
                .shopName(entity.getProduct().getUser().getShop_name())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .image(entity.getImage())
                .build();
    }
    public SKUEntity convertToEntity(SKUModel model){
        return SKUEntity.builder()
                .id(model.getId())
                .price(model.getPrice())
                .quantity(model.getQuantity())
                .image(model.getImage())
                .build();
    }
}
