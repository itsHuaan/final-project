package org.example.final_project.mapper;

import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.model.CartItemRequest;

public class OrderDetailMapper
{
    public static CartItemRequest toDTO(OrderDetailEntity entity){
        return CartItemRequest.builder()
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .shopId(entity.getShopId())
                .productId(entity.getProduct().getId())
                .option1(entity.getOption1())
                .option2(entity.getOption2())
                .build();
    }
}
