package org.example.final_project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartProductDto {
    private long productId;
    private String productName;
    private long categoryId;
    private String categoryName;
    private long shopId;
    private String shopName;
}
