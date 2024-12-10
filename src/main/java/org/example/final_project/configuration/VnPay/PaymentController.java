package org.example.final_project.configuration.VnPay;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.model.CartItemRequest;
import org.example.final_project.model.OrderModel;
import org.example.final_project.service.impl.OrderService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Slf4j
@RestController
@RequestMapping(Const.API_PREFIX + "/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OrderService orderService;

    @PostMapping("/create-payment")
    public ResponseEntity<?> submidOrder(@RequestBody OrderModel order,
                                         HttpServletRequest request) throws UnsupportedEncodingException {
        request.setAttribute("amount",order.getAmount());
        String tex = VnPayUtil.getRandomNumber(8);
        request.setAttribute("tex",tex);
        String vnpayUrl = orderService.submitCheckout(order, request);
        return ResponseEntity.ok().body(vnpayUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(orderService.statusPayment(request));
        }
       catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null));
        }
    }
    @GetMapping("/get-client-ip")
    public String getClientIpAddress() {
        String clientIp = VnPayUtil.getIpAddress(request);
        return "Client IP: " + clientIp;
    }


}
