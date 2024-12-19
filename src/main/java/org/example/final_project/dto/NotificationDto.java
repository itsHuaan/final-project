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
    private long recipientId;
    private long senderId;
    private int isRead;
    private String image;
    private LocalDateTime createdAt;
}
