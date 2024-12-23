package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserDto {
    private Long userId;
    private String profilePicture;
    private String name;
    private String username;
    private int newMessageCount;
    private ChatMessageDto lastMessage;
}
