package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.model.OrderModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMapper {
    UserMapper userMapper;

    public static OrderEntity toOrderEntity(OrderModel order) {
        return OrderEntity.builder()
                .shippingAddress(order.getAddressShipping())
                .methodCheckout(order.getMethodCheckout())
                .build();
    }

    public OrderDto toOrderDto(OrderEntity order) {
        UserDto userDto = userMapper.toDto(order.getUser());
        return OrderDto.builder()
                .shippingAddress(order.getShippingAddress())
                .methodCheckout(order.getMethodCheckout())
                .id(order.getId())
                .phoneReception(order.getPhoneReception())
                .totalPrice(order.getTotalPrice())
                .statusCheckout(order.getStatusCheckout())
                .createdAt(order.getCreatedAt())
                .orderCode(order.getOrderCode())
                .user(userDto)
                .customerName(order.getCustomerName())
                .build();
    }
}
