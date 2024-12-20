package org.example.final_project.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopStatisticDto {
    private int totalOfProducts;
    private int soldProducts;
    private int totalOfFeedbacks;
    private double averageRating;
    private int lockedProducts;
    private int outOfStockProducts;
    private int totalOfCustomers;
    private int totalOfOrders;
    private long revenue;
}
