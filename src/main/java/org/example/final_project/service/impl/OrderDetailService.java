package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.OrderTrackingEntity;
import org.example.final_project.mapper.OrderDetailMapper;
import org.example.final_project.mapper.OrderMapper;
import org.example.final_project.mapper.OrderTrackingMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IOrderTrackingRepository;
import org.example.final_project.service.IOrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderDetailService {
    IOrderRepository orderRepository;
    IOrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    IOrderTrackingRepository orderTrackingRepository;
    OrderMapper orderMapper;


    @Override
    public ApiResponse<?> getOrderDetail(long userId) {
        List<Long> orderId = orderRepository.findOrderIdsByUserId(userId);
        List<OrderDetailEntity> list = orderDetailRepository.findAllOrderDetailEntityByOrderId(orderId);

        List<OrderDetailDto> listDto = list.stream().map(orderDetailMapper::toOrderDto).toList();


        return ApiResponse.createResponse(HttpStatus.OK, "get all order tracking", listDto);


    }


    @Override
    public ApiResponse<?> getOrderDetailFlowShippingStatus(long userId, long shippingStatus) {
        List<Long> orderId = orderRepository.findOrderIdsByUserId(userId); // 2 3 5
        List<Long> orderIdTracking = orderDetailRepository.findOrderDetailsByStatus(shippingStatus);// 3 5 7

        List<Long> commonOrderIds = orderId.stream()
                .filter(orderIdTracking::contains)
                .toList();


        List<OrderDetailEntity> list = orderDetailRepository.findAllOrderDetailEntityByOrderId(commonOrderIds);
        List<OrderDetailDto> listDto = list.stream().map(orderDetailMapper::toOrderDto).toList();
        return ApiResponse.createResponse(HttpStatus.OK, "get all order tracking flow status", listDto);

    }

    @Override
    public ApiResponse<?> findDetailIn4OfOrder(long userId, long orderId, long shopId) {
        List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.shopOrder(shopId, orderId);
        List<OrderDetailDto> orderDetailDtos = orderDetailEntity.stream().map(orderDetailMapper::toOrderDto).toList();

        OrderTrackingEntity orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(orderId, shopId).get();

        OrderTrackingDto orderTrackingDto = OrderTrackingMapper.toOrderTrackingDto(orderTrackingEntity);
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        OrderEntity orderEntity1 = new OrderEntity();
        if (orderEntity.isPresent()) {
            orderEntity1 = orderEntity.get();
        }
        OrderDto orderDto = orderMapper.toOrderDto(orderEntity1);
        OrderTotalDto orderTotalDto1 = OrderTotalDto.builder()
                .orderDetails(orderDetailDtos)
                .orderTracking(orderTrackingDto)
                .order(orderDto)
                .build();
        return ApiResponse.createResponse(HttpStatus.OK, "get order detail", orderTotalDto1);
    }

    @Override
    public ApiResponse<?> findOrderInforByOrderCode(long userId, String orderCode) {
        long orderId = orderRepository.findOrderIdByUserIdAndOrderCode(userId, orderCode);
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findByOrderId(orderId);
        List<OrderDetailDto> orderDetailDtos = orderDetailEntities.stream().map(orderDetailMapper::toOrderDto).toList();
        return ApiResponse.createResponse(HttpStatus.OK, "get order by orderCode", orderDetailDtos);
    }

    @Override
    public int changeStatusRead(long orderDetailId) {
        Optional<OrderDetailEntity> orderDetailEntity = orderDetailRepository.findById(orderDetailId);
        if (orderDetailEntity.isPresent()) {
            OrderDetailEntity orderDetailEntity1 = orderDetailEntity.get();
            orderDetailEntity1.setHasFeedback(1);
            orderDetailRepository.save(orderDetailEntity1);
            return 1;
        }
        return 0;
    }


}
