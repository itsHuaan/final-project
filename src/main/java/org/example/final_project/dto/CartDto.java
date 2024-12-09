package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private Long cartId;
    private CartUserDto user;

    private int cartQuantity;
    private double totalPrice = 0;
    private Boolean status = true;

    private List<CartItemDto> cartItems;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
