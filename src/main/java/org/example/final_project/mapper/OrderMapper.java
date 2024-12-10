package org.example.final_project.mapper;

import org.example.final_project.entity.OrderEntity;
import org.example.final_project.model.OrderModel;

public class OrderMapper {
    public static OrderEntity toOrderEntity(OrderModel order) {
        return OrderEntity.builder()
                .shippingAddress(order.getAddressShipping())
                .methodCheckout(order.getMethodCheckout())
                .build();
    }
}
