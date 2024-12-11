package org.example.final_project.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.configuration.VnPay.VnPayUtil;
import org.example.final_project.entity.OrderEntity;
import org.example.final_project.model.OrderModel;
import org.example.final_project.service.IOrderService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/payment")
public class PaymentController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IOrderService orderService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> submitOrder(@RequestBody OrderModel order,
                                         HttpServletRequest request) throws Exception {
        request.setAttribute("amount",order.getAmount());
        String tex = VnPayUtil.getRandomNumber(8);
        request.setAttribute("tex",tex);
        String vnpayUrl = orderService.submitCheckout(order, request);
        return ResponseEntity.ok().body(vnpayUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<Void> paymentReturn(HttpServletRequest request) {
        try {
            orderService.statusPayment(request);
            return ResponseEntity.status(HttpStatus.FOUND) // 302 Redirect
                    .header("Location", "https://team03.cyvietnam.id.vn/en") // Redirect URL
                    .build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Location", "https://team03.cyvietnam.id.vn/en/error")
                    .build();
        }
    }

    @GetMapping("/get-order")
    public ResponseEntity<?> getOrder(@RequestParam long shopId) {
        return ResponseEntity.ok(createResponse(HttpStatus.OK,"ok",orderService.getOrderByShopIdAndOrderId(shopId)));
    }


}
