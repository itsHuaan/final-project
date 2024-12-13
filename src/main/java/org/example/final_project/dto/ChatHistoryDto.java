package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatHistoryDto {
    private String sender;
    private String profilePicture;
    private String message;
    private LocalDateTime sentAt;
}
