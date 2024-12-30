package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevenueAndGrowthRateStatisticDto {
    private List<Integer> month;
    private RevenueAndGrowthRateDto dataSet;
}
