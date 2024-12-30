package org.example.final_project.service;

import org.example.final_project.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticService {

    List<PeriodicStatisticDto> getPeriodicStatistics(long shopId);

    ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime);

    Page<CartSkuDto> getLowStockProducts(long shopId, int quantity, Pageable pageable);

    AdminStatisticDto getAdminStatisticDto();

    List<RevenueStatistic> getRevenueStatistics(long shopId, int year);

    List<RevenueStatistic> getRevenueStatistics(int year);

    RevenueAndGrowthRateStatisticDto getRevenueStatisticsTest(int year);
    RevenueAndGrowthRateStatisticDto getRevenueStatisticsTest(long shopId, int year);

    ShopRatioDto getShopRatioDto();
}
