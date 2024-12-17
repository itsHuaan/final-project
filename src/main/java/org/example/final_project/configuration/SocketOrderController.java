package org.example.final_project.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.service.IOrderDetailService;
import org.example.final_project.service.IOrderTrackingService;
import org.example.final_project.service.impl.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketOrderController {

    IOrderTrackingService iOrderTrackingService;
    SimpMessagingTemplate messagingTemplate;
    IOrderDetailService iOrderDetailService;
    @MessageMapping("/changeStatusShipping")
    public void changeStatus(@Payload StatusMessageDto statusMessageDto) {

        int result =  iOrderTrackingService.updateStatusShipping(statusMessageDto);
        StatusMessageDto responseDto = new StatusMessageDto();

        if(result == 1){
             responseDto = StatusMessageDto.builder()
                    .userId(statusMessageDto.getUserId())
                    .status(statusMessageDto.getStatus())
                    .shopId(statusMessageDto.getShopId())
                    .orderId(statusMessageDto.getOrderId())
                    .build();

        }
        messagingTemplate.convertAndSend("/user/sent", iOrderDetailService.findDetailIn4OfOrder(responseDto.getUserId(),responseDto.getOrderId(),responseDto.getShopId()));
    }





}
