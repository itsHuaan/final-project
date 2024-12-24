package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.ProductEntity;
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
        ProductEntity product = cartItemEntity.getProduct().getProduct();
        CartSkuDto item = variantMapper.toDto(cartItemEntity.getProduct());
        if (product.getIsActive() != 1) {
            return null;
        }
        return CartItemDto.builder()
                .cartDetailId(cartItemEntity.getCartDetailId())
                .item(variantMapper.toDto(cartItemEntity.getProduct()))
                .itemQuantity(cartItemEntity.getQuantity())
                .totalPrice(item.getDiscountedPrice() * cartItemEntity.getQuantity())
                .lastUpdated(cartItemEntity.getLastUpdated())
                .build();
    }

    public CartItemEntity toEntity(CartItemModel cartItemModel) {
        CartEntity cartEntity = cartRepository.findById(cartItemModel.getCartId()).orElseThrow(
                () -> new IllegalArgumentException("Cart not found")
        );
        SKUEntity product = skuRepository.findById(cartItemModel.getSkuId()).orElseThrow(
                () -> new IllegalArgumentException("Product not found")
        );
        return CartItemEntity.builder()
                .cart(cartEntity)
                .product(product)
                .quantity(cartItemModel.getQuantity())
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}
