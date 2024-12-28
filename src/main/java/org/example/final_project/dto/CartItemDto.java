package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDto {
    private Long cartDetailId;
    private CartSkuDto item;
    private Integer itemQuantity;
    private double totalPrice;
    private LocalDateTime lastUpdated;
}
