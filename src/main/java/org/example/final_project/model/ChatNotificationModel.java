package org.example.final_project.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatNotificationModel {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String message;
    private LocalDateTime sentAt;
}
