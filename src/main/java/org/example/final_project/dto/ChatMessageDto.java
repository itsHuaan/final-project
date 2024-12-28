package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto {
    private Long messageId;
    private String chatId;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private String message;
    private List<String> mediaUrls;
    private LocalDateTime sentAt;
    private Integer isSeen;
}
