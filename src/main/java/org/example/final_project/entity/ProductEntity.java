package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name="number_of_feedback")
    private long numberOfFeedBack;
    @Column(name="number_of_like")
    private long numberOfLike;
    private double rating;
    private String description;
    private long parent_id;
    private long quantity;
    private double price;
    private int isActive;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;
    @OneToMany(mappedBy = "productEntity")
    private List<ImageProductEntity> images;
    @OneToMany(mappedBy = "product")
    private List<FeedbackEntity> feedbacks;
}
