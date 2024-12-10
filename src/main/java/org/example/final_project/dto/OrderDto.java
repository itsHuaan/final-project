package org.example.final_project.dto;

import lombok.*;
import org.example.final_project.model.enum_status.CheckoutStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {
    private long id ;
    private Double totalPrice;
    private String shippingAddress;
    private String methodCheckout;

}
