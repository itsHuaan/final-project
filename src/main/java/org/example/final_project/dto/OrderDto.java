package org.example.final_project.dto;

import lombok.*;
import org.example.final_project.model.enum_status.CheckoutStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {
    private long id ;
    private Double totalPrice;
    private String phoneReception;
    private String shippingAddress;
    private long statusCheckout;
    private String methodCheckout;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String orderCode;
    private UserDto user;

}
