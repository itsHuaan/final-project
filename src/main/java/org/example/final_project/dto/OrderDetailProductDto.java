package org.example.final_project.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailProductDto {
    OrderDto order;
    OrderDetailDto orderDetails;
    OrderTrackingDto orderTracking;
}
