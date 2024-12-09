package org.example.final_project.configuration.VnPay;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping(Const.API_PREFIX + "/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/create-payment")
    public ResponseEntity<?> submidOrder(@RequestParam("amount") String amount,
                                         HttpServletRequest request) throws UnsupportedEncodingException {
        request.setAttribute("amount",amount);
        String vnpayUrl = paymentService.creatUrlPaymentForVnPay(request);
        log.info(vnpayUrl);
        return ResponseEntity.ok().body(vnpayUrl);
    }

    @PostMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if(status.equals("00")) {
            return ResponseEntity.ok("ok");
        }else {
            return ResponseEntity.ok("error");
        }
    }
    @GetMapping("/get-client-ip")
    public String getClientIpAddress() {
        String clientIp = VnPayUtil.getIpAddress(request);
        return "Client IP: " + clientIp;
    }


}
