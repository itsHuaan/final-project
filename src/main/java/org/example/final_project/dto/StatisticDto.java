package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatisticDto {
    private int totalOfProducts;
    private long soldProducts;
    private int totalOfFeedbacks;
    private double averageRating;
    private int lockedProducts;
    private long totalOfCustomers;
    private int totalOfOrders;
    private double revenue;
}
