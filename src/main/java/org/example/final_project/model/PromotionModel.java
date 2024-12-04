package org.example.final_project.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionModel {
    private String name;
    private double discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int status;
}
