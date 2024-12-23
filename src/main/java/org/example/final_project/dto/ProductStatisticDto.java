package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductStatisticDto {
    private long productId;
    private String productName;
    private long numberOfFeedBack;
    private long numberOfLike;
    private double rating;
    private long sold;
    private long totalQuantity;
    private String image;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
