package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductSummaryDto {
    private long productId;
    private String productName;
    private long numberOfFeedBack;
    private long numberOfLike;
    private double rating;
    private long sold;
    private long totalQuantity;
    private CategorySummaryDto category;
    private String image;
    private ShopDto shopDto;
    private double oldPrice;
    private double newPrice;
    private double discountPercentage;
    private Integer status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
