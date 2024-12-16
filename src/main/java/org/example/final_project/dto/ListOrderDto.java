package org.example.final_project.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ListOrderDto {
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
    private List<SKUDto> skuDtos;
}
