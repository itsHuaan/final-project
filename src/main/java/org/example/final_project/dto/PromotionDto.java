package org.example.final_project.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

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
    private int status;
}
