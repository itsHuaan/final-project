package org.example.final_project.dto;

import lombok.*;

import java.awt.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusMessageDto {
    private TrayIcon.MessageType type;
    private String content;
    private int status;
    private long shopId;
    private long orderId;
}
