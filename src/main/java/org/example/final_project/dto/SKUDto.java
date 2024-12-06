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
    private ProductOptionDto option;
    private ProductOptionValueDto value;
    private double price;
    private long quantity;
    private String image;
}
