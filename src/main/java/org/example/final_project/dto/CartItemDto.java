package org.example.final_project.dto;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.SKUEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDto {
    private Long cartItemId;
    private Long cartId;
    private SKUDto product;
    private Integer quantity;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
