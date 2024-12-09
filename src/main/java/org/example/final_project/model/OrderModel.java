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
    private long productId;
    private long shopId;
    private long statusCheckout;
    private String methodCheckout;
    private Double totalPrice;
}
