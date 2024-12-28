package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemModel {
    private Long cartId;
    private Long skuId;
    private int quantity;
}
