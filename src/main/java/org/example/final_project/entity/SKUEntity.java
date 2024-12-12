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


    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;


    @ManyToOne
    @JoinColumn(name="option_id1")
    private ProductOptionsEntity option1;

    @ManyToOne
    @JoinColumn(name="option_id2")
    private ProductOptionsEntity option2;

    @ManyToOne
    @JoinColumn(name="value_id1")
    private ProductOptionValuesEntity value1;

    @ManyToOne
    @JoinColumn(name="value_id2")
    private ProductOptionValuesEntity value2;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;

    @OneToMany(mappedBy = "skuEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetailEntity> orderDetails;
}
