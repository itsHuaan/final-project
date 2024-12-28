package org.example.final_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Size(max = 4000)
    private String description;
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


    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "product")
    private List<SKUEntity> skuEntities;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteProductEntity> favorites = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="tbl_product_promotion",joinColumns = @JoinColumn(name="product_id"),inverseJoinColumns = @JoinColumn(name="promotion_id"))
    List<PromotionEntity> promotions;
}
