package org.example.final_project.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.final_project.entity.Category;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private long id;
    private String name;
    private long numberOfFeedBack;
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
    private CategoryDto categoryDto;
}
