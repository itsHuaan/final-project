package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoomDto {
    private String chatId;
    private String sender;
    private String recipient;
    private List<ChatHistoryDto> messages;
}
