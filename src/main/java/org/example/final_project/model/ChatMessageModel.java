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
@ToString
public class ChatMessageModel {
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String message;
    private List<MultipartFile> mediaFiles;
    private List<String> mediaUrls;
    private LocalDateTime sentAt;
}
