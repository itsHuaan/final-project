package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    private long productSkuId;
    private long cartDetailId;
    private long quantity;
    private long shopId;
    private double price;
    private long option1;
    private long option2;
    private String nameProduct;
}
