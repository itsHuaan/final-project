package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.CartModel;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;

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
                .user(userMapper.toCartUserDto(cartEntity.getUser()))
                .cartQuantity(cartEntity.getCartItems().size())
                .cartItems(cartEntity.getCartItems().stream()
                        .sorted(Comparator.comparing(CartItemEntity::getCreatedAt).reversed())
                        .map(cartItemMapper::toDto)
                        .toList())
                .createdAt(cartEntity.getCreatedAt())
                .modifiedAt(cartEntity.getModifiedAt())
                .build();
    }

    public CartEntity toEntity(CartModel cartModel) {
        UserEntity userEntity = userRepository.findById(cartModel.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid User: " + cartModel.getUserId())
        );

        return CartEntity.builder()
                .user(userEntity)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
