package org.example.final_project.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderTotalDto {
    private OrderDto order;
    private List<OrderDetailDto> orderDetails;
    private OrderTrackingDto orderTracking;
}
