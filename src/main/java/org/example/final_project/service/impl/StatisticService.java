package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShopStatisticDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.specification.OrderDetailSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.example.final_project.specification.OrderDetailSpecification.hasShop;
import static org.example.final_project.specification.ProductSpecification.*;
import static org.example.final_project.util.Const.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService implements IStatisticService {
    IProductRepository productRepository;
    IOrderRepository orderRepository;
    IOrderDetailRepository orderDetailRepository;

    private ShopStatisticDto buildStatistic(long shopId, LocalDateTime start, LocalDateTime end) {
        return ShopStatisticDto.builder()
                .averageRating(getAverageOfRating(shopId, start, end))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId, start, end))
                .totalOfProducts(getTotalProducts(shopId, start, end))
                .totalOfOrders(getTotalOfOrders(shopId, start, end))
                .build();
    }

    @Override
    public List<ShopStatisticDto> getStaticStatistic(long shopId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));

        return List.of(
                buildStatistic(shopId, startOfDay, CURRENT_DATE),
                buildStatistic(shopId, START_OF_WEEK, CURRENT_DATE),
                buildStatistic(shopId, START_OF_MONTH, CURRENT_DATE),
                buildStatistic(shopId, START_OF_YEAR, CURRENT_DATE)
        );
    }

    @Override
    public ShopStatisticDto getStatistic(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return buildStatistic(shopId, startTime, endTime);
    }

    private double getAverageOfRating(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        ofShop(shopId))
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
                        ofShop(shopId))
                .and(isValid())
                .and(isBetween(startTime, endTime))).stream().toList();
        return products.stream()
                .mapToInt(product -> product.getFeedbacks().stream().toList().size())
                .sum();
    }

    private int getTotalProducts(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findAll(Specification.where(
                        ofShop(shopId))
                .and(isNotDeleted())
                .and(isBetween(startTime, endTime))).size();
    }

    private int getTotalOfOrders(long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderDetailRepository.findAll(Specification.where(
                hasShop(shopId)
                        .and(OrderDetailSpecification.isBetween(startTime, endTime))
        )).size();
    }
}
