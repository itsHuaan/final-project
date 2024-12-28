package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionDto {
    private long id;
    private String name;
    private double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductSummaryDto> products;
}
