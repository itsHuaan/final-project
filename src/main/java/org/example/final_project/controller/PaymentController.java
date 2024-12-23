package org.example.final_project.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.VnPay.VnPayUtil;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.NotifyModel;
import org.example.final_project.model.OrderModel;
import org.example.final_project.service.IOrderService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(Const.API_PREFIX + "/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    IOrderService orderService;

    SimpMessagingTemplate messagingTemplate;


    @PostMapping("/create-payment")
    public ResponseEntity<?> submitOrder(@RequestBody OrderModel order,
                                         HttpServletRequest request) throws Exception {
        request.setAttribute("amount", order.getAmount());
        String tex = VnPayUtil.getRandomNumber(8);
        request.setAttribute("tex", tex);
        if (order.getMethodCheckout().equalsIgnoreCase("vnpay")) {
            String vnpayUrl = orderService.submitCheckout(order, request);
            return ResponseEntity.ok().body(vnpayUrl);
        } else {
            Set<Long> sentShopIds = new HashSet<>();
            List<CartItemRequest> cartItemRequest = order.getCartItems();
            for (CartItemRequest cartItem : cartItemRequest) {
                if (!sentShopIds.contains(cartItem.getShopId())) {
                    long shopId = cartItem.getShopId();
                    long userId = order.getUserId();
                    NotifyModel notifyModel1 = NotifyModel.builder()
                            .userId(userId)
                            .shopId(shopId)
                            .notifyTitle("có đơn hàng được đặt của " + userId + shopId)
                            .build();
                    sentShopIds.add(cartItem.getShopId());
                    messagingTemplate.convertAndSend("/note/notify", notifyModel1);
                }
            }
            return ResponseEntity.ok().body(orderService.submitCheckout(order, request));
        }

    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> paymentReturn(HttpServletRequest request) {
        try {
            orderService.statusPayment(request);
            String vnp_TxnRef = (String) request.getAttribute("tex");
            String amount = orderService.getTotalPrice(vnp_TxnRef);
            OrderModel order = orderService.sentNotify(request);
            Set<Long> sentShopIds = new HashSet<>();
            List<CartItemRequest> cartItemRequest = order.getCartItems();
            for (CartItemRequest cartItem : cartItemRequest) {
                if (!sentShopIds.contains(cartItem.getShopId())) {
                    long shopId = cartItem.getShopId();
                    long userId = order.getUserId();
                    NotifyModel notifyModel1 = NotifyModel.builder()
                            .userId(userId)
                            .shopId(shopId)
                            .notifyTitle("có đơn hàng được đặt của " + userId)
                            .build();
                    sentShopIds.add(cartItem.getShopId());
                    messagingTemplate.convertAndSend("/note/notify", notifyModel1);
                }
            }
            String redirectUrl = String.format(
                    "https://team03.cyvietnam.id.vn/en/checkoutsuccess?tex=%s&amount=%s",
                    vnp_TxnRef, amount
                    ,
                    "success"
            );


            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception ex) {
            String vnp_TxnRef = (String) request.getAttribute("tex");
            String amount = orderService.getTotalPrice(vnp_TxnRef);
            String redirectUrl = String.format(
                    "https://team03.cyvietnam.id.vn/en/checkoutfail?tex=%s&amount=%s",
                    vnp_TxnRef, amount
                    ,
                    "success"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Location", redirectUrl)
                    .build();
        }
    }


}
