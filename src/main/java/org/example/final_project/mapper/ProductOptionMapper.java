package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductFamilyDto;
import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionMapper {
    ProductOptionValueMapper valueMapper;
    IProductOptionValueRepository valueRepository;
    public ProductOptionDetailDto convertToDto(ProductOptionsEntity optionsEntity){
        return ProductOptionDetailDto.builder()
                .id(optionsEntity.getId())
                .name(optionsEntity.getName())
                .values(valueRepository.findAllByOption_Id(optionsEntity.getId()).stream().map(valueMapper::convertToDto).collect(Collectors.toList()))
                .build();
    }

    public ProductOptionDto convertToDtoWithValue(ProductOptionsEntity optionsEntity, ProductOptionValuesEntity valueEntity) {
        ProductOptionValueDto valueDto = valueEntity != null
                ? ProductOptionValueDto.builder()
                .valueId(valueEntity.getId())
                .name(valueEntity.getName())
                .build()
                : null;

        return ProductOptionDto.builder()
                .optionId(optionsEntity.getId())
                .name(optionsEntity.getName())
                .value(valueDto)
                .build();
    }


    public ProductOptionsEntity convertToEntity(ProductOptionsModel model){
        return ProductOptionsEntity.builder()
                .name(model.getName())
                .build();
    }
}
