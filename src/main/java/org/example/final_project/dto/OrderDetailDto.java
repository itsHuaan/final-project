package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDto {
    private long id;
    private long shopId;
    private long orderId;
    private long quantity;
    private Double price;
    private long option1;
    private long option2;
    private String productName;
    private UserDto user;
    private LocalDateTime createdAt;
    private int shippingStatus;
    private SKUDto skuDto;
    private Integer hasFeedback;
}
