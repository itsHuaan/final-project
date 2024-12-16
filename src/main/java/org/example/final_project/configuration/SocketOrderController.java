package org.example.final_project.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.service.IOrderTrackingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketOrderController {

    IOrderTrackingService iOrderTrackingService;

    @MessageMapping("/changeStatusShipping")
    @SendTo("/topic/public")
    public StatusMessageDto changeStatus(@Payload StatusMessageDto statusMessageDto) {
        int status = statusMessageDto.getStatus();
        long shopId = statusMessageDto.getShopId();
        long orderId = statusMessageDto.getOrderId();
        int result =  iOrderTrackingService.updateStatusShipping(status, shopId, orderId);

        if (result == 1) {
            statusMessageDto.setContent("Status update success for Order ID: " + orderId);
            statusMessageDto.setStatus(status);
            statusMessageDto.setShopId(shopId);
            statusMessageDto.setOrderId(orderId);
            return statusMessageDto;
        } else {
            statusMessageDto.setContent("Status update failed for Order ID: " + orderId);
        }
        return statusMessageDto;
    }
}
