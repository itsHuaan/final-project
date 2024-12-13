package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.OrderTrackingEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.OrderDetailMapper;
import org.example.final_project.mapper.OrderTrackingMapper;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IOrderTrackingRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IOrderDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderDetailService {
    IOrderRepository orderRepository;
    IOrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    IUserRepository userRepository;
    IOrderTrackingRepository orderTrackingRepository;


    @Override
    public ApiResponse<?> getOrderDetail(long userId ) {
        List<Long> orderId = orderRepository.findOrderIdsByUserId(userId);
        List<OrderDetailEntity> list = orderDetailRepository.findAllOrderDetailEntityByOrderId(orderId);

        List<OrderDetailDto> listDto = list.stream().map(orderDetailMapper::toOrderDto).toList();



        return ApiResponse.createResponse(HttpStatus.OK,"get all order tracking", listDto);


    }


    @Override
    public ApiResponse<?> getOrderDetailFlowShippingStatus(long userId , long shippingStatus){
        List<Long> orderId = orderRepository.findOrderIdsByUserId(userId);
        List<OrderDetailEntity> list = orderDetailRepository.findOrderDetailsByOrderTrackingStatusZeroAndOrderId(shippingStatus,orderId);
        List<OrderDetailDto> listDto = list.stream().map(orderDetailMapper::toOrderDto).toList();

        return ApiResponse.createResponse(HttpStatus.OK,"get all order tracking flow status", listDto);

    }
    @Override
    public ApiResponse<?> findDetailIn4OfOrder(long userId, long oderId , long shopId) {
       List<OrderDetailEntity> orderDetailEntity = orderDetailRepository.shopOrder(shopId,oderId);
       List<OrderDetailDto> orderDetailDtos = orderDetailEntity.stream().map(orderDetailMapper::toOrderDto).toList();

        OrderTrackingEntity orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(oderId,shopId ).get();

        OrderTrackingDto orderTrackingDto = OrderTrackingMapper.toOrderTrackingDto(orderTrackingEntity);
        OrderTotalDto orderTotalDto1 = OrderTotalDto.builder()
                .orderDetails(orderDetailDtos)
                .orderTracking(orderTrackingDto)
                .order(null)
                .build();

       return ApiResponse.createResponse(HttpStatus.OK,"get order detail", orderTotalDto1);
    }







}
