package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationModel {
    private String title;
    private String content;
    private long recipientId;
    private long senderId;
    private String image;
    private LocalDateTime createdAt;

}
