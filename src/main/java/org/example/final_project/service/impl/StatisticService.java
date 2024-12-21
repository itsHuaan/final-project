package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.dto.PeriodicStatisticDto;
import org.example.final_project.dto.ShopStatisticDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.VariantMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.specification.OrderDetailSpecification;
import org.example.final_project.specification.SKUSpecification;
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
    VariantMapper variantMapper;
    IOrderRepository orderRepository;

    private PeriodicStatisticDto buildStatistic(long shopId, String period, LocalDateTime startTime) {
        return PeriodicStatisticDto.builder()
                .period(period)
                .averageRating(getAverageOfRating(shopId, startTime, END_OF_DAY))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, startTime, END_OF_DAY))
                .totalOfProducts(getTotalProducts(shopId, startTime, END_OF_DAY))
                .totalOfOrders(getTotalOfOrders(shopId, startTime, END_OF_DAY))
                .revenue(getRevenue(shopId, startTime, END_OF_DAY))
                .lockedProducts(getLockedProducts(shopId, startTime, END_OF_DAY))
                .totalOfCustomers(getTotalCustomers(shopId, startTime, END_OF_DAY))
                .soldProducts(getSoldProducts(shopId, startTime, END_OF_DAY))
                .build();
    }

    @Override
    public List<PeriodicStatisticDto> getPeriodicStatistics(long shopId) {
        return List.of(
                buildStatistic(shopId, "Today", START_OF_DAY),
                buildStatistic(shopId, "This week", START_OF_WEEK),
                buildStatistic(shopId, "This month", START_OF_MONTH),
                buildStatistic(shopId, "This year", START_OF_YEAR)
        );
    }

    @Override
    public ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return ShopStatisticDto.builder()
                .averageRating(getAverageOfRating(shopId, startTime, endTime))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, startTime, endTime))
                .totalOfProducts(getTotalProducts(shopId, startTime, endTime))
                .totalOfOrders(getTotalOfOrders(shopId, startTime, endTime))
                .revenue(getRevenue(shopId, startTime, endTime))
                .lockedProducts(getLockedProducts(shopId, startTime, endTime))
                .lowStockProducts(getLowStockProducts(shopId))
                .totalOfCustomers(getTotalCustomers(shopId, startTime, endTime))
                .soldProducts(getSoldProducts(shopId, startTime, endTime))
                .build();
    }

    private double getAverageOfRating(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(shopId))
                .and(isValid())
                .and(isBetween(startTime, endTime))).stream().toList();
        return products.stream()
                .mapToDouble(product -> product.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .average()
                .orElse(0.0);
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

    private List<CartSkuDto> getLowStockProducts(long shopId) {
        return skuRepository.findAll(Specification.where(
                        SKUSpecification.hasShop(shopId)
                                .and(SKUSpecification.hasRatingAbove(3.0))
                                .and(SKUSpecification.isLowStock())
                )).stream()
                .map(variantMapper::toDto)
                .toList();
    }

    private int getLockedProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(
                hasUserId(shopId)
                        .and(isNotStatus(1))
                        .and(isBetween(startTime, endTime))).size();
    }

    private long getTotalCustomers(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(OrderDetailSpecification.distinctUsersByShopIdAndDateRange(shopId, startTime, endTime)).stream()
                .map(orderDetail -> orderDetail.getOrderEntity().getUser().getUserId())
                .distinct()
                .count();
    }

    private int getSoldProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return 0;
    }

}
