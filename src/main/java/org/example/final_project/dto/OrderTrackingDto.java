package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderTrackingDto {
    private long id;
    private int status;
    private LocalDateTime createdAt;
    private String note;
    private long shopId;
    private LocalDateTime paidDate;
    private List<HistoryStatusShippingDto> historyStatusShippingDtoList;
}
