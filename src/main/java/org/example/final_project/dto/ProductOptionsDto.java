package org.example.final_project.dto;

import lombok.*;
import org.example.final_project.entity.ProductStockEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductOptionsDto {
    private long id;
    private String name;
    private double price;
    private ProductDto productDto;
}
