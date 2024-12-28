package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.CartDto;
import org.example.final_project.dto.CheckoutDto;
import org.example.final_project.model.CartModel;

import java.util.List;

public interface ICartService extends IBaseService<CartDto, CartModel, Long> {
    CartDto getUserCart(Long userId);

    CheckoutDto getCheckOutDetail(Long cartId, List<Long> selectedCartItemIds);

    ApiResponse<?> checkShopStatusWhenCheckOut(Long shopId);
}
