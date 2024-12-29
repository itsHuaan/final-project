package org.example.final_project.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.VnPay.VnPayUtil;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.NotifyModel;
import org.example.final_project.model.OrderModel;
import org.example.final_project.service.IOrderService;
import org.example.final_project.service.IUserService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    IUserService userService;


    private void notifyShops(OrderModel order) {
        Set<Long> sentShopIds = new HashSet<>();
        List<CartItemRequest> cartItemRequest = order.getCartItems();
        for (CartItemRequest cartItem : cartItemRequest) {
            if (!sentShopIds.contains(cartItem.getShopId())) {
                long shopId = cartItem.getShopId();
                long userId = order.getUserId();
                UserDto userDto = userService.getById(userId);
                NotifyModel notifyModel1 = NotifyModel.builder()
                        .userId(userId)
                        .shopId(shopId)
                        .notifyTitle("Người dùng" + " " + userDto.getName() + " " + "vừa đặt hàng")
                        .build();
                sentShopIds.add(cartItem.getShopId());
                messagingTemplate.convertAndSend("/note/notify", notifyModel1);
            }
        }
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> submitOrder(@RequestBody OrderModel order,
                                         HttpServletRequest request) throws Exception {


        List<CartItemRequest> cartItemRequest = order.getCartItems();
        for (CartItemRequest cartItem : cartItemRequest) {
            int result = orderService.checkQuatity(cartItem.getProductSkuId(), cartItem.getQuantity());
            if (result == 0) {
                request.setAttribute("amount", order.getAmount());
                String tex = VnPayUtil.getRandomNumber(8);
//                String orderCode = orderService.checkOrderCode(tex);
                request.setAttribute("tex", tex);
                if (order.getMethodCheckout().equalsIgnoreCase("vnpay")) {
                    String vnpayUrl = orderService.submitCheckout(order, request);
                    return ResponseEntity.ok().body(vnpayUrl);
                } else {
                    notifyShops(order);
                    return ResponseEntity.ok().body(orderService.submitCheckout(order, request));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The current quantity is greater than the quantity in the stock");
    }

    @GetMapping("/vnpay-return")
    public ModelAndView paymentReturn(HttpServletRequest request) {
        try {
            orderService.getPaymentStatus(request);
            String vnp_TxnRef = (String) request.getAttribute("tex");
            String amount = orderService.getTotalPrice(vnp_TxnRef);
            OrderModel order = orderService.sentNotify(request);

            if (order == null) {
                return new ModelAndView("redirect:https://team03.cyvietnam.id.vn/en/checkoutfail?tex="
                        + vnp_TxnRef + "&amount=" + amount);
            } else {
                notifyShops(order);
                return new ModelAndView("redirect:https://team03.cyvietnam.id.vn/en/checkoutsuccess?tex="
                        + vnp_TxnRef + "&amount=" + amount);
            }
        } catch (Exception ex) {
            String vnp_TxnRef = (String) request.getAttribute("tex");
            String amount = orderService.getTotalPrice(vnp_TxnRef);
            return new ModelAndView("redirect:https://team03.cyvietnam.id.vn/en/checkoutfail?tex="
                    + vnp_TxnRef + "&amount=" + amount);
        }
    }


}
