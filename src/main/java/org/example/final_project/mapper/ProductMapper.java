package org.example.final_project.mapper;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IImageProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryRepository iCategoryRepository;
    @Autowired
    IImageProductRepository imageProductRepository;
    @Autowired
    ImageProductMapper imageMapper;
    public ProductDto convertToDto(ProductEntity productEntity){
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .numberOfLike(productEntity.getNumberOfLike())
                .numberOfFeedBack(productEntity.getNumberOfFeedBack())
                .rating(productEntity.getRating())
                .description(productEntity.getDescription())
                .parent_id(productEntity.getParent_id())
                .quantity(productEntity.getQuantity())
                .price(productEntity.getPrice())
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
                .isActive(productEntity.getIsActive())
                .categoryDto(categoryMapper.convertToDto(productEntity.getCategoryEntity()))
                .images(imageProductRepository.findAllByProductEntity_Id(productEntity.getId()).stream().map(x->imageMapper.convertToDto(x)).collect(Collectors.toList()))
                .build();
    }
    public ProductEntity convertToEntity(ProductModel model){
        return ProductEntity.builder()
                .name(model.getName())
                .numberOfLike(model.getNumberOfLike())
                .numberOfFeedBack(model.getNumberOfFeedBack())
                .rating(model.getRating())
                .description(model.getDescription())
                .parent_id(model.getParent_id())
                .quantity(model.getQuantity())
                .price(model.getPrice())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .categoryEntity(iCategoryRepository.findById(model.getCategoryId()).get())
                .build();
    }
}
