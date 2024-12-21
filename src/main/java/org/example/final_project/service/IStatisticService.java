package org.example.final_project.service;

import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.dto.PeriodicStatisticDto;
import org.example.final_project.dto.ShopStatisticDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticService {

    List<PeriodicStatisticDto> getPeriodicStatistics(long shopId);

    ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime);

    Page<CartSkuDto> getLowStockProducts(long shopId, int quantity, Pageable pageable);
}
