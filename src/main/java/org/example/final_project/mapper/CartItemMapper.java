package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.CartItemModel;
import org.example.final_project.repository.ICartRepository;
import org.example.final_project.repository.ISKURepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemMapper {
    VariantMapper variantMapper;
    ICartRepository cartRepository;
    ISKURepository skuRepository;

    public CartItemDto toDto(CartItemEntity cartItemEntity) {
        return CartItemDto.builder()
                .cartDetailId(cartItemEntity.getCartDetailId())
                .item(variantMapper.toDto(cartItemEntity.getProduct()))
                .itemQuantity(cartItemEntity.getQuantity())
                .totalPrice(cartItemEntity.getProduct().getPrice() * cartItemEntity.getQuantity())
                .build();
    }

    public CartItemEntity toEntity(CartItemModel cartItemModel) {
        CartEntity cartEntity = cartRepository.findById(cartItemModel.getCartDetailId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid cartDetailId: " + cartItemModel.getCartDetailId())
        );
        SKUEntity product = skuRepository.findById(cartItemModel.getSkuId()).orElseThrow(
                () -> new IllegalArgumentException("Invalid skuId: " + cartItemModel.getSkuId())
        );
        return CartItemEntity.builder()
                .cart(cartEntity)
                .product(product)
                .quantity(cartItemModel.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
