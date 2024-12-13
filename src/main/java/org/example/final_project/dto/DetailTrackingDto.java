package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetailTrackingDto {
    private OrderTrackingDto orderTrackingDto;
    private OrderDetailDto orderDetailDto;

}
