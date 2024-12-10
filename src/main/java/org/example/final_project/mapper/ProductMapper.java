package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductFamilyDto;
import org.example.final_project.dto.ProductSummaryDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.ISKUService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductMapper {
    CategoryMapper categoryMapper;
    ICategoryRepository iCategoryRepository;
    IImageProductRepository imageProductRepository;
    ImageProductMapper imageMapper;
    ISKUService iskuService;
    UserMapper userMapper;
    FeedbackMapper feedbackMapper;

    public ProductDto convertToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
//                .numberOfLike(productEntity.getNumberOfLike())
                .numberOfFeedBack(productEntity.getFeedbacks().size())
                .rating(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .description(productEntity.getDescription())
                .note(productEntity.getNote())
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
                .isActive(productEntity.getIsActive())
                .category(categoryMapper.convertToDto(productEntity.getCategoryEntity()))
                .images(imageProductRepository.findAllByProductEntity_Id(productEntity.getId()).stream().map(imageMapper::convertToDto).collect(Collectors.toList()))
                .variants(iskuService.getAllByProduct(productEntity.getId()))
                .shop(userMapper.toShopDto(productEntity.getUser()))
                .feedbacks(productEntity.getFeedbacks().stream().map(feedbackMapper::convertToDto).toList())
                .build();
    }

    public ProductEntity convertToEntity(ProductModel model) {
        return ProductEntity.builder()
                .name(model.getName())
                .description(model.getDescription())
                .note(model.getNote())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .categoryEntity(iCategoryRepository.findById(model.getCategoryId()).isPresent()
                        ? iCategoryRepository.findById(model.getCategoryId()).get()
                        : null)
                .build();
    }

    public ProductFamilyDto toProductFamilyDto(ProductEntity productEntity){
        return ProductFamilyDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .productImage(productEntity.getImages() != null
                        ? productEntity.getImages().get(0).getImageLink()
                        : null)
                .categoryId(productEntity.getCategoryEntity().getId())
                .categoryName(productEntity.getCategoryEntity().getName())
                .shopId(productEntity.getUser().getUserId())
                .shopName(productEntity.getUser().getShop_name())
                .build();
    }

    public ProductSummaryDto toProductSummaryDto(ProductEntity productEntity){
        return ProductSummaryDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfFeedBack(productEntity.getFeedbacks().size())
                .rating(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
                .category(categoryMapper.toCategorySummaryDto(productEntity.getCategoryEntity()))
                .images(imageProductRepository.findAllByProductEntity_Id(productEntity.getId()).stream().map(imageMapper::convertToDto).collect(Collectors.toList()))
                .build();
    }
}
