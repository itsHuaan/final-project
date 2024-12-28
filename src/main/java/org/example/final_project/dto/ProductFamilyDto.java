package org.example.final_project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductFamilyDto {
    private long productId;
    private String productName;
    private String productImage;
    private long categoryId;
    private String categoryName;
    private long shopId;
    private String shopName;
}
