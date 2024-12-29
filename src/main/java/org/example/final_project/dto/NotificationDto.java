package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {
    private long id;
    private String title;
    private String content;
    private Long userId;
    private Long adminId;
    private int isRead;
    private String image;
    private Long shopId;
    private Long shopUserId;
    private Long orderId;
    private String orderCode;
    private LocalDateTime createdAt;
}
