package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShopStatisticDto;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IStatisticService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static org.example.final_project.specification.ProductSpecification.isNotDeleted;
import static org.example.final_project.specification.ProductSpecification.ofShop;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService implements IStatisticService {
    IProductRepository productRepository;

    @Override
    public ShopStatisticDto getStatistic(long shopId) {
        return ShopStatisticDto.builder()
                .totalOfProducts(getTotalProducts(shopId))
                .build();
    }

    private int getTotalProducts(long shopId) {
        return productRepository.findAll(Specification.where(ofShop(shopId)).and(isNotDeleted())).size();
    }
}
