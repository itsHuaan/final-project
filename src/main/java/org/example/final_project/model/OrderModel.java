package org.example.final_project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderModel {
    private long userId;
    private String addressShipping;
    private String methodCheckout;
    private String phoneReception;
    private String amount;
    private String customerName;
    private List<CartItemRequest> cartItems;
}
