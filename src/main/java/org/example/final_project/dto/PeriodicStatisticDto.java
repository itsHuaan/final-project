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
    private int totalOfProducts;
    private int soldProducts;
    private int totalOfFeedbacks;
    private double averageRating;
    private int lockedProducts;
    private long totalOfCustomers;
    private int totalOfOrders;
    private double revenue;
}
