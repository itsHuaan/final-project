package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderStatisticByStatusDto {
    private String status;
    private long order;
    private double ratio;
}
