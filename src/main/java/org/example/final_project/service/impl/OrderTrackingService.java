package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.entity.OrderTrackingEntity;
import org.example.final_project.model.enum_status.CheckoutStatus;
import org.example.final_project.model.enum_status.StatusShipping;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IOrderRepository;
import org.example.final_project.repository.IOrderTrackingRepository;
import org.example.final_project.service.IOrderTrackingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderTrackingService implements IOrderTrackingService {
    IOrderTrackingRepository orderTrackingRepository;
    IOrderDetailRepository orderDetailRepository;
    IOrderRepository orderRepository;


    @Override
    public int updateStatusShipping(StatusMessageDto messageDto) {
        Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(messageDto.getOrderId(), messageDto.getShopId());
//        List<OrderDetailEntity> orderDetailEntityList = orderDetailRepository.shopOrder(messageDto.getShopId(), messageDto.getOrderId());
        if (orderTrackingEntity.isPresent()) {

            OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
            if (messageDto.getStatus() == StatusShipping.Completed.getStatus()) {
                orderTrackingEntity1.setPaidDate(LocalDateTime.now());
                Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(messageDto.getOrderId());
                if (optionalOrderEntity.isPresent()) {
                    OrderEntity orderEntity = optionalOrderEntity.get();
                    orderEntity.setStatusCheckout(CheckoutStatus.Completed.getStatus());
                    orderRepository.save(orderEntity);
                }
            }
            orderTrackingEntity1.setStatus(messageDto.getStatus());
            orderTrackingEntity1.setNote(messageDto.getNote());
            orderTrackingRepository.save(orderTrackingEntity1);
            return 1;
        }
        return 0;
    }

}
