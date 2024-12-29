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
    private ProductOptionDto option1;
    private ProductOptionDto option2;
    private double price;
    private double discountedPrice;
    private long quantity;
    private String image;
}
