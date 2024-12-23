package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PeriodicStatisticDto {
    private String period;
    private ShopStatisticDto statistics;
}
