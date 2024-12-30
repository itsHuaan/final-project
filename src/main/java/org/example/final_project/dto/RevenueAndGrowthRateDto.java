package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevenueAndGrowthRateDto {
    private String revenue;
    private List<Double> revenueData;
    private String growthRate;
    private List<Double> growthRateData;
}
