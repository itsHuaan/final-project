package org.example.final_project.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoomDto {
    private Long id;
    private String chatId;

    private Long senderId;
    private Long recipientId;
}
