package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> mediaUrls;
    private LocalDateTime sentAt;
}
