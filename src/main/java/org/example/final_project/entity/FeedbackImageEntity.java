package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="feedback_image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="image_link")
    private String imageLink;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name="feedback_id")
    private FeedbackEntity feedback;
}
