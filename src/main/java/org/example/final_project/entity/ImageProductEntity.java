package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_image_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String imageLink;
    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity productEntity;
}
