package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_product_options")
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


    @OneToMany(mappedBy = "option")
    private List<ProductOptionValuesEntity> valuesEntities;


    @OneToMany(mappedBy = "option")
    private List<SKUEntity> skuEntities;
}
