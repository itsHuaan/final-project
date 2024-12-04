package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_sku_option_value")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name="sku_id")
    private SKUEntity skuEntity;


    @ManyToOne
    @JoinColumn(name="option_id")
    private ProductOptionsEntity optionsEntity;


    @ManyToOne
    @JoinColumn(name="value_id")
    private ProductOptionValuesEntity valuesEntity;
}
