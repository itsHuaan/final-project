package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopRatioDto {
    private long totalShops;
    private long totalLockedShops;
    private long totalPendingShop;
    private long totalRejectedShops;
}
