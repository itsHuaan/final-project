package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.CartModel;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartMapper {
    UserMapper userMapper;
    CartItemMapper cartItemMapper;
    IUserRepository userRepository;

    public CartDto toDto(CartEntity cartEntity) {
        return CartDto.builder()
                .cartId(cartEntity.getCartId())
                .user(userMapper.toDto(cartEntity.getUser()))
                .cartQuantity(cartEntity.getCartItems().size())
                .cartItems(cartEntity.getCartItems().stream().map(cartItemMapper::toDto).toList())
                .totalPrice(cartEntity.getTotalPrice())
                .createdAt(cartEntity.getCreatedAt())
                .build();
    }

    public CartEntity toEntity(CartModel cartModel) {
        UserEntity userEntity = userRepository.findById(cartModel.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid User: " + cartModel.getUserId())
        );

        return CartEntity.builder()
                .user(userEntity)
                .totalPrice(cartModel.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
