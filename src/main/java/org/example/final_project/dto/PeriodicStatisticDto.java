package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PeriodicStatisticDto {
    private String period;
    private StatisticDto statistics;
}
