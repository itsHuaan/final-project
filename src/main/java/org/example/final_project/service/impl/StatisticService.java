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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.final_project.specification.OrderDetailSpecification.hasShop;
import static org.example.final_project.specification.ProductSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService implements IStatisticService {
    IProductRepository productRepository;
    IOrderRepository orderRepository;
    IOrderDetailRepository orderDetailRepository;

    @Override
    public ShopStatisticDto getStatistic(long shopId) {
        return ShopStatisticDto.builder()
                .averageRating(getAverageOfRating(shopId))
                .totalOfFeedbacks(getTotalOfFeedbacks(shopId))
                .totalOfProducts(getTotalProducts(shopId))
                .totalOfOrders(getTotalOfOrders(shopId))
                .build();
    }

    private double getAverageOfRating(long shopId) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(ofShop(shopId)).and(isValid())).stream().toList();
        return products.stream()
                .mapToDouble(product -> product.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .average()
                .orElse(0.0);
    }

    private int getTotalOfFeedbacks(long shopId) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(ofShop(shopId)).and(isValid())).stream().toList();
        return products.stream()
                .mapToInt(product -> product.getFeedbacks().stream().toList().size())
                .sum();
    }

    private int getTotalProducts(long shopId) {
        return productRepository.findAll(Specification.where(ofShop(shopId)).and(isNotDeleted())).size();
    }

    private int getTotalOfOrders(long shopId) {
        return orderDetailRepository.findAll(hasShop(shopId)).size();
    }
}
