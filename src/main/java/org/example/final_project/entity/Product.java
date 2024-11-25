package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.service.impl.CategoryService;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
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
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
