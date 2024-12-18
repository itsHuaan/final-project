package org.example.final_project.service;

import org.example.final_project.dto.ShopStatisticDto;

public interface IStatisticService {

    ShopStatisticDto getStatistic(long shopId);
}
