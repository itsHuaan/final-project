package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionMapper {
    ProductOptionValueMapper valueMapper;
    IProductOptionValueRepository valueRepository;
    ProductOptionValueMapper productOptionValueMapper;

    public ProductOptionDetailDto convertToDto(ProductOptionsEntity optionsEntity) {
        return ProductOptionDetailDto.builder()
                .id(optionsEntity.getId())
                .name(optionsEntity.getName())
                .values(valueRepository.findAllByOption_Id(optionsEntity.getId()).stream().map(valueMapper::convertToDto).collect(Collectors.toList()))
                .build();
    }

    public ProductOptionDto toProductOptionDto(ProductOptionValuesEntity valueEntity) {
        return valueEntity != null
                ?ProductOptionDto.builder()
                .optionId(valueEntity.getOption().getId())
                .name(valueEntity.getOption().getName())
                .value(productOptionValueMapper.convertToDto(valueEntity))
                .build()
                : null;
    }

    public ProductOptionsEntity convertToEntity(ProductOptionsModel model) {
        return ProductOptionsEntity.builder()
                .name(model.getName())
                .build();
    }
}
