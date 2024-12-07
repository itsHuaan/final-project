package org.example.final_project.mapper;

import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
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
    @Autowired
    ProductOptionMapper optionMapper;

    public SKUDto convertToDto(SKUEntity entity) {
        ProductOptionsEntity option1 = entity.getOption1();
        ProductOptionsEntity option2 = entity.getOption2();
        return SKUDto.builder()
                .id(entity.getId())
                .option1(option1!=null
                        ? optionMapper.convertToDto1(entity.getOption1())
                        : null)
                .option2(option2 != null
                        ? optionMapper.convertToDto1(entity.getOption2())
                        : null)
                .cartProductDto(cartProductMapper.toDto(entity.getProduct()))
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
