package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductFamilyDto;
import org.example.final_project.dto.ProductSummaryDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.ISKUService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    IProductRepository productRepository;

    public ProductDto convertToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfLike(productEntity.getFavorites().size())
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
                .feedbacks(productEntity.getFeedbacks().stream()
                        .sorted(Comparator.comparing(FeedbackEntity::getCreatedAt).reversed())
                        .map(feedbackMapper::convertToDto)
                        .toList())
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

    public ProductSummaryDto toProductSummaryDto(ProductEntity productEntity) {
        List<SKUEntity> variants = productEntity.getSkuEntities();
        return ProductSummaryDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfLike(productEntity.getFavorites().size())
                .numberOfFeedBack(productEntity.getFeedbacks().size())
                .rating(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
                .category(categoryMapper.toCategorySummaryDto(productEntity.getCategoryEntity()))
                .image(imageProductRepository.findAllByProductEntity_Id(productEntity.getId())
                        .stream()
                        .map(imageMapper::convertToDto)
                        .findFirst()
                        .map(ImageProductDto::getImageLink)
                        .orElse(null))
                .price(variants.stream()
                        .map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0))
                .shopDto(userMapper.toShopDto(productEntity.getUser()))
                .status(productEntity.getIsActive())
                .build();
    }
}
