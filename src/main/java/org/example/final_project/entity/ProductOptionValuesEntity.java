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


    @OneToMany(mappedBy = "value")
    private List<SKUEntity> skuEntities;


    @ManyToOne
    @JoinColumn(name="option_id")
    private ProductOptionsEntity option;
}
