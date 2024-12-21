package org.example.final_project.service;

import org.example.final_project.dto.PeriodicStatisticDto;
import org.example.final_project.dto.ShopStatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticService {

    List<PeriodicStatisticDto> getPeriodicStatistics(long shopId);

    ShopStatisticDto getStatistics(long shopId, LocalDateTime startTime, LocalDateTime endTime);
}
