package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderStatisticDto {
    private long totalOfOrders;
    private List<OrderStatisticByStatusDto> orderByStatus;
}
