package org.example.final_project.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutDto {
    private UserDto userDto;           // Tên người dùng
    private List<CartItemDto> cartItems; // Danh sách sản phẩm trong giỏ
    private double totalPrice;         // Tổng tiền sản phẩm
}
