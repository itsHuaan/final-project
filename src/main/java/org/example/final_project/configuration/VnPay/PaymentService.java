package org.example.final_project.configuration.VnPay;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VnPayConfig config;

    public String creatUrlPaymentForVnPay(HttpServletRequest request) {
        String amountParam = request.getParameter("amount");
        if (amountParam == null || amountParam.isEmpty()) {
            throw new IllegalArgumentException("Amount parameter is missing or invalid");
        }
        long amount;
        try {
            amount = Integer.parseInt(amountParam) * 100L;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount parameter must be a valid number", e);
        }
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = config.getVNPayConfig();
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_IpAddr", "127.0.0.1");
        String queryUrl = VnPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecure = VnPayUtil.hmacSHA512(config.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecure;
        String paymentUrl = config.getVnp_PayUrl() + "?" + queryUrl;
        return paymentUrl;
    }
}
