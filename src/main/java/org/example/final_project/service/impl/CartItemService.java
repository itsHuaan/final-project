package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.mapper.CartItemMapper;
import org.example.final_project.model.CartItemModel;
import org.example.final_project.repository.ICartItemRepository;
import org.example.final_project.repository.ICartRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.ICartItemService;
import org.example.final_project.util.specification.CartItemSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.example.final_project.util.specification.CartItemSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemService implements ICartItemService {
    ICartItemRepository cartItemRepository;
    ICartRepository cartRepository;
    ISKURepository skuRepository;
    CartItemMapper cartItemMapper;

    /*@Override
    public CartItemDto getCartItem(Long cartId, Long productId) {
        return cartItemMapper.toDto(
                cartItemRepository.findOne(Specification.where(
                                hasCartId(cartId).and(hasProductId(productId))))
                        .orElseGet(
                                () -> {
                                    CartItemEntity newCartItemEntity = new CartItemEntity();
                                    newCartItemEntity.setCart(cartRepository.findById(cartId).orElseThrow(
                                            () -> new IllegalArgumentException("Cart not found")
                                    ));
                                    newCartItemEntity.setProduct(skuRepository.findById(productId).orElseThrow(
                                            () -> new IllegalArgumentException("Product not found")
                                    ));
                                    newCartItemEntity.setQuantity(0);
                                    newCartItemEntity.setCreatedAt(LocalDateTime.now());
                                    return cartItemRepository.save(newCartItemEntity);
                                }
                        )
        );
    }*/

    @Override
    public CartItemDto getCartItem(Long cartId, Long productId) {
        CartItemEntity cartItemEntity = cartItemRepository.findOne(Specification.where(
                hasCartId(cartId).and(hasProductId(productId)))
        ).orElseGet(() -> {
            SKUEntity product = skuRepository.findById(productId).orElseThrow(
                    () -> new IllegalArgumentException("Product not found")
            );

            CartEntity cart = cartRepository.findById(cartId).orElseThrow(
                    () -> new IllegalArgumentException("Cart not found")
            );

            CartItemEntity newCartItemEntity = new CartItemEntity();
            newCartItemEntity.setCart(cart);
            newCartItemEntity.setProduct(product);
            newCartItemEntity.setQuantity(0);
            newCartItemEntity.setCreatedAt(LocalDateTime.now());
            return cartItemRepository.save(newCartItemEntity);
        });

        return cartItemMapper.toDto(cartItemEntity);
    }


    @Override
    public int updateQuantity(Long cartId, Long productId, Integer quantity, boolean isAddingOne) {
        CartItemEntity currentCartItem = cartItemRepository.findOne(Specification.where(
                hasCartId(cartId).and(hasProductId(productId))
        )).orElse(null);

        SKUEntity product = skuRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product not found")
        );

        if (currentCartItem != null) {
            int newQuantity = isAddingOne
                    ? currentCartItem.getQuantity() + quantity
                    : quantity;

            if (newQuantity > product.getQuantity()) {
                throw new IndexOutOfBoundsException("Requested quantity exceeds available stock");
            }

            currentCartItem.setQuantity(newQuantity);
            cartItemRepository.save(currentCartItem);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteCartItems(Long cartId, List<Long> productIds) {
        Specification<CartItemEntity> specification = Specification.where(hasCartId(cartId));
        List<CartItemEntity> cartItems;
        if (productIds == null || productIds.isEmpty()) {
            cartItems = cartItemRepository.findAll(specification);
        } else {
            specification = specification.and(hasProductIds(productIds));
            cartItems = cartItemRepository.findAll(specification);
        }
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            return cartItems.size();
        }
        return 0;
    }

    @Override
    public List<CartItemDto> getAll() {
        return List.of();
    }

    @Override
    public CartItemDto getById(Long id) {
        return null;
    }

    @Override
    public int save(CartItemModel cartItemModel) {
        return 0;
    }

    @Override
    public int update(Long aLong, CartItemModel cartItemModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
