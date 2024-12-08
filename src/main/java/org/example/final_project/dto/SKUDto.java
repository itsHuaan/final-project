package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SKUDto {
    private long variantId;
    private ProductOptionDto option1;
    private ProductOptionDto option2;
    private double price;
    private long quantity;
    private String image;
}
