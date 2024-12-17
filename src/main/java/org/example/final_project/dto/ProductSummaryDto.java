package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private CategorySummaryDto category;
    private String image;
    private ShopDto shopDto;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
