package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageDto {
    private Long id;

    private String chatId;

    private Long senderId;
    private Long recipientId;
    private String message;
    private LocalDateTime sentAt;
}
