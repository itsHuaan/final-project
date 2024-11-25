package org.example.final_project.mapper;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.Product;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryRepository iCategoryRepository;
    public ProductDto convertToDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .numberOfLike(product.getNumberOfLike())
                .numberOfFeedBack(product.getNumberOfFeedBack())
                .rating(product.getRating())
                .description(product.getDescription())
                .parent_id(product.getParent_id())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .deletedAt(product.getDeletedAt())
                .isActive(product.isActive())
                .categoryDto(categoryMapper.convertToDto(product.getCategory()))
                .build();
    }
    public Product convertToEntity(ProductModel model){
        return Product.builder()
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
                .isActive(model.isActive())
                .category(iCategoryRepository.findById(model.getCategoryId()).get())
                .build();
    }
}
