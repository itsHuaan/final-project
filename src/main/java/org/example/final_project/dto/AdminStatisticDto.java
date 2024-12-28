package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminStatisticDto {
    private long totalUsers;
    private long totalNewUsers;
    private long totalLockedUsers;
    private long totalShops;
    private long totalLockedShops;
    private long totalPendingShop;
    private long totalRejectedShops;
    private long totalLockedProducts;
    private long totalRejectedProducts;
    private List<ShopDto> topSellerShops;
}
