package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notification_")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String image;
    private String content;
    private Long userId;
    private int isRead;
    @Column(name = "adminId", nullable = true)
    private Long adminId;
    private Long shopId;
    private Long shopUserId;
    private Long orderId;
    private String orderCode;
    private LocalDateTime createdAt;
}
