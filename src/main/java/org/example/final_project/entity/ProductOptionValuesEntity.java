package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tbl_option_values")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductOptionValuesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;


    @OneToMany(mappedBy = "value1")
    private List<SKUEntity> skuEntities1;


    @OneToMany(mappedBy = "value2")
    private List<SKUEntity> skuEntities2;


    @ManyToOne
    @JoinColumn(name="option_id")
    private ProductOptionsEntity option;
}
