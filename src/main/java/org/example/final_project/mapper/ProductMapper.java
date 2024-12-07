package org.example.final_project.mapper;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.ISKUService;
import org.example.final_project.service.IUserService;
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
    @Autowired
    ISKUService iskuService;
    @Autowired
    IUserService iUserService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ShopMapper shopMapper;

    public ProductDto convertToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfLike(productEntity.getNumberOfLike())
                .numberOfFeedBack(productEntity.getNumberOfFeedBack())
                .rating(productEntity.getRating())
                .description(productEntity.getDescription())
                .note(productEntity.getNote())
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
                .isActive(productEntity.getIsActive())
                .category(categoryMapper.convertToDto(productEntity.getCategoryEntity()))
                .images(imageProductRepository.findAllByProductEntity_Id(productEntity.getId()).stream().map(x -> imageMapper.convertToDto(x)).collect(Collectors.toList()))
                .variants(iskuService.getAllByProduct(productEntity.getId()))
                .shop(shopMapper.toDto(productEntity.getUser()))
                .build();
    }

    public ProductEntity convertToEntity(ProductModel model) {
        return ProductEntity.builder()
                .name(model.getName())
                .numberOfLike(model.getNumberOfLike())
                .numberOfFeedBack(model.getNumberOfFeedBack())
                .rating(model.getRating())
                .description(model.getDescription())
                .note(model.getNote())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .categoryEntity(iCategoryRepository.findById(model.getCategoryId()).get())
                .build();
    }
}
