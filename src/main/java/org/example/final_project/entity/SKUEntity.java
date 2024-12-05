package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tbl_stock_keeping_unit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SKUEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double price;
    private long quantity;
    private String image;

    // một sản phẩm gồm nhiều sku
    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;


    @ManyToOne
    @JoinColumn(name="option_id")
    private ProductOptionsEntity option;


    @ManyToOne
    @JoinColumn(name="value_id")
    private ProductOptionValuesEntity value;

    @OneToMany(mappedBy = "product")
    private List<CartItemEntity> cartItems;
}
