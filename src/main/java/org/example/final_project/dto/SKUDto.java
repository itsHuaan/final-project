package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SKUDto {
    private long id;
    private String productName;
    private String shopName;
    private long shopId;
    private ProductOptionDto option1;
    private ProductOptionValueDto value1;
    private ProductOptionDto option2;
    private ProductOptionValueDto value2;
    private double price;
    private long quantity;
    private String image;
}
