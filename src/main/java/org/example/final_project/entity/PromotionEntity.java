package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_promotion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name="discount_percentage")
    private double discountPercentage;
    @Column(name="start_date")
    private LocalDateTime startDate;
    @Column(name="end_date")
    private LocalDateTime endDate;
    @ManyToMany(mappedBy = "promotions")
    private List<ProductEntity> products;
    private LocalDateTime deletedAt;
}
