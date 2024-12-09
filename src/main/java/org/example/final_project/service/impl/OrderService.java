package org.example.final_project.service.impl;

import jakarta.persistence.criteria.Order;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.OrderModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    public int submitCheckout(OrderModel orderModel) {
        OrderEntity orderEntity = new OrderEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(orderModel.getUserId());
        orderEntity.setUser(userEntity);
        orderEntity.setMethod_checkout(orderModel.getMethodCheckout());
        orderEntity.setTotal_price(orderModel.getTotalPrice());
        return 1;
    }
}
