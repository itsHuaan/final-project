package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HistoryStatusShippingDto {
    private long id;
    private int status;
    private LocalDateTime createdChangeStatus;
    
}
