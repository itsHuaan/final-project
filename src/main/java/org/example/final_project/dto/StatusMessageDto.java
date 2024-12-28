package org.example.final_project.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusMessageDto {
    private String note;
    private String content;
    private long userId;
    private int status;
    private long shopId;
    private long orderId;
}
