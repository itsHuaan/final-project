package org.example.final_project.dto;


import lombok.*;

import java.util.List;

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
    private List<CartSkuDto> lowStockProducts;
    private int totalOfCustomers;
    private int totalOfOrders;
    private double revenue;
}
