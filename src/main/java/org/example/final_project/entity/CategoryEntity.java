package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long parent_id;
    private int isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String image;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @OneToMany(mappedBy = "categoryEntity")
    private List<ProductEntity> productEntities;
}
