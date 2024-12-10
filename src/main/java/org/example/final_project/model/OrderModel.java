package org.example.final_project.model;

import lombok.*;
import org.example.final_project.model.enum_status.CheckoutStatus;

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
}
