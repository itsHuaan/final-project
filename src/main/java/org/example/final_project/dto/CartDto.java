package org.example.final_project.dto;

import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private Long cartId;
    private UserDto user;

    private int quantity;
    private double totalPrice = 0;
    private Boolean status = true;

    private List<CartItemDto> cartItems;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
