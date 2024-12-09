package org.example.final_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_feedback")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String content;
    @Min(1)
    @Max(5)
    private double rate;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;
    @OneToMany(mappedBy = "feedback")
    private List<FeedbackImageEntity> feedbackImages;
    private LocalDateTime createdAt;
}
