package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SKUDto {
    private long productId;
    private long variantId;
    private ProductOptionDto option1;
    private ProductOptionDto option2;
    private double oldPrice;
    private double newPrice;
    private long quantity;
    private String image;
}
