package org.example.final_project.mapper;

import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductOptionMapper {
    @Autowired
    ProductOptionValueMapper valueMapper;
    @Autowired
    IProductOptionValueRepository valueRepository;
    public ProductOptionDetailDto convertToDto(ProductOptionsEntity optionsEntity){
        return ProductOptionDetailDto.builder()
                .id(optionsEntity.getId())
                .name(optionsEntity.getName())
                .values(valueRepository.findAllByOption_Id(optionsEntity.getId()).stream().map(x->valueMapper.convertToDto(x)).collect(Collectors.toList()))
                .build();
    }
    public ProductOptionDto convertToDto1(ProductOptionsEntity optionsEntity){
        return ProductOptionDto.builder()
                .id(optionsEntity.getId())
                .name(optionsEntity.getName())
                .build();
    }
    public ProductOptionsEntity convertToEntity(ProductOptionsModel model){
        return ProductOptionsEntity.builder()
                .name(model.getName())
                .build();
    }
}
