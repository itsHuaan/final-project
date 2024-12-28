package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private long productId;
    private String productName;
    private long numberOfFeedBack;
    private long numberOfLike;
    private double rating;
    private long sold;
    private String description;
    private int isActive;
    private long totalQuantity;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private CategoryDto category;
    private List<ImageProductDto> images;
    private double oldPrice;
    private double newPrice;
    private double discountPercentage;
    private List<SKUDto> variants;
    private ShopDto shop;
    private List<FeedbackDto> feedbacks;
}
