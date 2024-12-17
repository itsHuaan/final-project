package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartDetailId;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private SKUEntity product;

    private Integer quantity;
    private LocalDateTime lastUpdated;
    private LocalDateTime deletedAt;
}
