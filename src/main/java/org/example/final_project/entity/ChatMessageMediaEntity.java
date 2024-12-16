package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_chat_media")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mediaUrl;
    @ManyToOne
    @JoinColumn(name="chat_message_id")
    private ChatMessageEntity chatMessage;
}
