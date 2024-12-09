package org.example.final_project.service;

import org.example.final_project.dto.CartItemDto;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.model.CartItemModel;

import java.util.List;

public interface ICartItemService extends IBaseService<CartItemDto, CartItemModel, Long> {
    CartItemDto getCartItem(Long cartId, Long productId);

    int updateQuantity(Long cartId, Long productId, Integer quantity, boolean isAddingOne);

    int deleteCartItems(Long cartId, List<Long> productIds);
}
