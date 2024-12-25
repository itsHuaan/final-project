package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevenueStatistic {
    private int month;
    private double revenue;
    private double growthRate;
}
