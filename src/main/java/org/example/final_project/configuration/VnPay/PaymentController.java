package org.example.final_project.configuration.VnPay;


import jakarta.servlet.http.HttpServletRequest;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(Const.API_PREFIX + "/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create-payment")
    public ResponseEntity<?> submidOrder(@RequestParam("amount") String amount,
                                         HttpServletRequest request) throws UnsupportedEncodingException {
        request.setAttribute("amount",amount);
        String vnpayUrl = paymentService.creatUrlPaymentForVnPay(request);
        return ResponseEntity.ok().body(vnpayUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if(status.equals("00")) {
            return ResponseEntity.ok("ok");
        }else {
            return ResponseEntity.ok("error");
        }
    }


}
