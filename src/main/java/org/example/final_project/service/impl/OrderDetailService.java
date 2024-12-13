package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.OrderDetailDto;
import org.example.final_project.entity.OrderDetailEntity;
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
    public ApiResponse<?> findDetailIn4OfOrder(long userId, long orderDetailId) {
       OrderDetailEntity orderDetailEntity = orderDetailRepository.findOrderDetailByOrderDetailIdAndUserId(orderDetailId,userId).orElse(null);
       OrderDetailDto orderDetailDto = orderDetailMapper.toOrderDto(orderDetailEntity);
       return ApiResponse.createResponse(HttpStatus.OK,"get order detail", orderDetailDto);
    }







}
