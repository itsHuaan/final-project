package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_product_stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quantity;


    @ManyToOne
    @JoinColumn(name="product_option_id")
    private ProductOptionsEntity productOption;


    @ManyToOne
    @JoinColumn(name = "product_option_value_id")
    private ProductOptionValuesEntity productOptionValue;
}
