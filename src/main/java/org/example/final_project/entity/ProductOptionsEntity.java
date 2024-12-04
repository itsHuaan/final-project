package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tbl_product_options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductOptionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;


    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;


    @OneToMany(mappedBy = "productOption")
    private List<ProductStockEntity> productStocks;
}
