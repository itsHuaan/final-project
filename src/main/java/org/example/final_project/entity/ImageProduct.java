package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="image_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String imageLink;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
}
