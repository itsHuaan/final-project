package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.OrderDetailDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderTrackingRepository;
import org.example.final_project.service.impl.OrderDetailService;
import org.example.final_project.service.impl.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailMapper
{
    private UserService userService;
    private SKUMapper skuMapper;


    public static CartItemRequest toDTO(OrderDetailEntity entity){
        return CartItemRequest.builder()
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .shopId(entity.getShopId())
                .productSkuId(entity.getSkuEntity().getId())
                .option1(entity.getOption1())
                .option2(entity.getOption2())
                .build();
    }

    public   OrderDetailDto toOrderDto(OrderDetailEntity orderDetailEntity){
        SKUDto skuDto = skuMapper.convertToDto(orderDetailEntity.getSkuEntity());
        return OrderDetailDto.builder()
                .id(orderDetailEntity.getId())
                .price(orderDetailEntity.getPrice())
                .quantity(orderDetailEntity.getQuantity())
                .shopId(orderDetailEntity.getShopId())
                .option1(orderDetailEntity.getOption1())
                .option2(orderDetailEntity.getOption2())
                .createdAt(orderDetailEntity.getCreateAt())
                .productName(orderDetailEntity.getNameProduct())
                .user(userService.getById(orderDetailEntity.getShopId()))
                .orderId(orderDetailEntity.getOrderEntity().getId())
                .shippingStatus(orderDetailEntity.getStatusShip())
                .skuDto(skuDto)
                .build();
    }
}
