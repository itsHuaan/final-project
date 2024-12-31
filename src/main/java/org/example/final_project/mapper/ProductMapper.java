package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.IPromotionService;
import org.example.final_project.service.ISKUService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
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
    IPromotionService promotionService;

    public ProductDto convertToDto(ProductEntity productEntity) {
        List<SKUEntity> variants = productEntity.getSkuEntities();
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
                .oldPrice(variants.stream()
                        .map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0)
                )
                .discountPercentage(promotionService.findAllPromotionByNow(productEntity.getId()) != null ?
                        promotionService.findAllPromotionByNow(productEntity.getId()).getDiscountPercentage()
                        : 0.0)
                .newPrice(promotionService.findAllPromotionByNow(productEntity.getId()) != null
                        ? variants.stream().map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0) * (100 - promotionService.findAllPromotionByNow(productEntity.getId()).getDiscountPercentage()) / 100
                        : variants.stream().map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0))
                .feedbacks(productEntity.getFeedbacks().stream()
                        .sorted(Comparator.comparing(FeedbackEntity::getCreatedAt).reversed())
                        .map(feedbackMapper::convertToDto)
                        .toList())
                .totalQuantity(productEntity.getSkuEntities().stream()
                        .mapToInt(variant -> Math.toIntExact(variant.getQuantity()))
                        .sum())
                .sold(productEntity.getSkuEntities().stream()
                        .flatMap(sku -> sku.getOrderDetails().stream())
                        .mapToLong(OrderDetailEntity::getQuantity)
                        .sum())
                .build();
    }

    public ProductEntity convertToEntity(ProductModel model) {
        return ProductEntity.builder()
                .name(model.getName())
                .description(model.getDescription())
                .note(model.getNote())
                .categoryEntity(iCategoryRepository.findById(model.getCategoryId()).orElse(null))
                .build();
    }

    public ProductFamilyDto toProductFamilyDto(ProductEntity productEntity) {
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
                .rating(Math.round(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0) * 100.0) / 100.0)
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
                .oldPrice(variants.stream()
                        .map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0)
                )
                .discountPercentage(promotionService.findAllPromotionByNow(productEntity.getId()) != null ?
                        promotionService.findAllPromotionByNow(productEntity.getId()).getDiscountPercentage()
                        : 0.0)
                .newPrice(promotionService.findAllPromotionByNow(productEntity.getId()) != null
                        ? variants.stream().map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0) * (100 - promotionService.findAllPromotionByNow(productEntity.getId()).getDiscountPercentage()) / 100
                        : variants.stream().map(SKUEntity::getPrice)
                        .min(Double::compareTo)
                        .orElse(0.0))
                .shopDto(userMapper.toShopDto(productEntity.getUser()))
                .status(productEntity.getIsActive())
                .note(productEntity.getNote())
                .totalQuantity(productEntity.getSkuEntities().stream()
                        .mapToInt(variant -> Math.toIntExact(variant.getQuantity()))
                        .sum())
                .sold(variants.stream()
                        .flatMap(sku -> sku.getOrderDetails().stream())
                        .mapToLong(OrderDetailEntity::getQuantity)
                        .sum())
                .build();
    }

    public ProductStatisticDto toProductStatisticDto(ProductEntity productEntity) {
        List<SKUEntity> variants = productEntity.getSkuEntities();
        return ProductStatisticDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfLike(productEntity.getFavorites().size())
                .numberOfFeedBack(productEntity.getFeedbacks().size())
                .rating(Math.round(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0) * 100.0) / 100.0)
                .createdAt(productEntity.getCreatedAt())
                .modifiedAt(productEntity.getModifiedAt())
                .deletedAt(productEntity.getDeletedAt())
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
                .totalQuantity(productEntity.getSkuEntities().stream()
                        .mapToInt(variant -> Math.toIntExact(variant.getQuantity()))
                        .sum())
                .sold(variants.stream()
                        .flatMap(sku -> sku.getOrderDetails().stream())
                        .mapToLong(OrderDetailEntity::getQuantity)
                        .sum())
                .build();
    }
}
