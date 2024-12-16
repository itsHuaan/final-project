package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.entity.OrderTrackingEntity;
import org.example.final_project.repository.IOrderTrackingRepository;
import org.example.final_project.service.IOrderTrackingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderTrackingService implements IOrderTrackingService {
    IOrderTrackingRepository orderTrackingRepository;


    @Override
    public int updateStatusShipping(StatusMessageDto messageDto){
        Optional<OrderTrackingEntity> orderTrackingEntity = orderTrackingRepository.findByOrderIdAndShopId(messageDto.getOrderId(), messageDto.getShopId());
        if(orderTrackingEntity.isPresent()){
            OrderTrackingEntity orderTrackingEntity1 = orderTrackingEntity.get();
            orderTrackingEntity1.setStatus(messageDto.getStatus());
            orderTrackingRepository.save(orderTrackingEntity1);
            return 1;
        }
        return 0;
    }

}
