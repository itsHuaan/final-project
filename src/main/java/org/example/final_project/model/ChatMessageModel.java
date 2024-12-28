package org.example.final_project.model;


import lombok.*;

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
    private List<String> mediaUrls;
    private LocalDateTime sentAt;
}
