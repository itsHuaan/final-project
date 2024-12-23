package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.mapper.SKUMapper;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.mapper.VariantMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.specification.OrderDetailSpecification;
import org.example.final_project.specification.SKUSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.specification.OrderDetailSpecification.hasShop;
import static org.example.final_project.specification.ProductSpecification.*;
import static org.example.final_project.util.Const.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService implements IStatisticService {
    IProductRepository productRepository;
    ISKURepository skuRepository;
    IOrderDetailRepository orderDetailRepository;
    SKUMapper skuMapper;
    UserMapper userMapper;
    ProductMapper productMapper;
    VariantMapper variantMapper;

    private ShopStatisticDto buildStatistic(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return ShopStatisticDto.builder()
                .averageRating(getAverageOfRating(shopId, startTime, endTime))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, startTime, endTime))
                .totalOfProducts(getTotalProducts(shopId, startTime, endTime))
                .totalOfOrders(getTotalOfOrders(shopId, startTime, endTime))
                .revenue(getRevenue(shopId, startTime, endTime))
                .lockedProducts(getLockedProducts(shopId, startTime, endTime))
                .totalOfCustomers(getTotalCustomers(shopId, startTime, endTime))
                .soldProducts(getSoldProducts(shopId, startTime, endTime))
                .topPurchasedUsers(getTopPurchasedUsers(shopId, startTime, endTime))
                .topPurchasedProducts(getTopPurchasedProducts(shopId, startTime, endTime))
                .build();
    }

    @Override
    public List<PeriodicStatisticDto> getPeriodicStatistics(long shopId) {
        return List.of(
                new PeriodicStatisticDto("Today", buildStatistic(shopId, START_OF_DAY, END_OF_DAY)),
                new PeriodicStatisticDto("This week", buildStatistic(shopId, START_OF_WEEK, END_OF_DAY)),
                new PeriodicStatisticDto("This month", buildStatistic(shopId, START_OF_MONTH, END_OF_DAY)),
                new PeriodicStatisticDto("This year", buildStatistic(shopId, START_OF_YEAR, END_OF_DAY))
        );
    }

    @Override
    public ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return buildStatistic(shopId, startTime, endTime);
    }

    private double getAverageOfRating(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isValid())
                .and(isBetween(startTime, endTime))).stream().toList();

        double averageRating = products.stream()
                .mapToDouble(product -> product.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .average()
                .orElse(0.0);
        return Math.round(averageRating * 100.0) / 100.0;
    }

    private int getTotalOfFeedbacks(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isValid())
                .and(isBetween(startTime, endTime))).stream().toList();
        return products.stream()
                .mapToInt(product -> product.getFeedbacks().stream().toList().size())
                .sum();
    }

    private int getTotalProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isNotDeleted())
                .and(isBetween(startTime, endTime))).size();
    }

    private int getTotalOfOrders(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                hasShop(shopId)
                        .and(OrderDetailSpecification.isBetween(startTime, endTime))
        )).size();
    }

    private double getRevenue(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                        hasShop(shopId).and(OrderDetailSpecification.isBetween(startTime, endTime))
                )).stream()
                .mapToDouble(orderDetail -> orderDetail.getQuantity() * orderDetail.getPrice())
                .sum();
    }

    @Override
    public Page<CartSkuDto> getLowStockProducts(long shopId, int quantity, Pageable pageable) {
        return skuRepository.findAll(Specification.where(
                        SKUSpecification.hasShop(shopId)
                                .and(SKUSpecification.isValid())
                                .and(SKUSpecification.isLowStock(quantity))
                ), pageable)
                .map(variantMapper::toDto);
    }

    private int getLockedProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(
                hasUserId(shopId)
                        .and(isNotDeleted())
                        .and(isNotStatus(1))
                        .and(isBetween(startTime, endTime))).size();
    }

    private long getTotalCustomers(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(OrderDetailSpecification.distinctUsersByShopIdAndDateRange(shopId, startTime, endTime)).stream()
                .map(orderDetail -> orderDetail.getOrderEntity().getUser().getUserId())
                .distinct()
                .count();
    }

    private long getSoldProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                        OrderDetailSpecification.hasShop(shopId)
                                .and(OrderDetailSpecification.isBetween(startTime, endTime))
                )).stream()
                .mapToLong(OrderDetailEntity::getQuantity)
                .sum();
    }


    private List<UserFeedBackDto> getTopPurchasedUsers(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findDistinctUsersByShopAndDateRange(shopId, startTime, endTime, PageRequest.of(0, 10)).getContent().stream()
                .map(userMapper::toUserFeedBackDto)
                .toList();
    }

    private List<ProductStatisticDto> getTopPurchasedProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findTopPurchasedProductsByShop(shopId, startTime, endTime,
                        PageRequest.of(0, 10)).getContent().stream()
                .map(productMapper::toProductStatisticDto)
                .toList();
    }
}
