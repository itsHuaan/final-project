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
    private Long id;
    private String name;


    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<ProductOptionValuesEntity> valuesEntities;


    @OneToMany(mappedBy = "option1", cascade = CascadeType.ALL)
    private List<SKUEntity> skuEntities1;


    @OneToMany(mappedBy = "option2", cascade = CascadeType.ALL)
    private List<SKUEntity> skuEntities2;
}
