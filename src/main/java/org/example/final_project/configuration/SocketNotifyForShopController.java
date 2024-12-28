//package org.example.final_project.configuration;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.example.final_project.model.NotifyModel;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class SocketNotifyForShopController {
//    SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/notify-shop")
//    public void notifyForShop(@Payload NotifyModel notifyModel) {
//        long userId = notifyModel.getUserId();
//        long shopId = notifyModel.getShopId();
//
//
//        NotifyModel notifyModel1 = NotifyModel.builder()
//                .userId(userId)
//                .shopId(shopId)
//                .notifyTitle("có đơn hàng được đặt của " + userId)
//                .build();
//        messagingTemplate.convertAndSend("/note/notify", notifyModel1);
//    }
//}
