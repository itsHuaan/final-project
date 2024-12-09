package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartSkuDto {
    private long itemId;
    private ProductFamilyDto productFamily;
    private ProductOptionValueDto value1;
    private ProductOptionValueDto value2;
    private double price;
    private long quantity;
    private String image;
}
