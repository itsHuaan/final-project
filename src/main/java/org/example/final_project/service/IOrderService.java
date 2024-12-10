package org.example.final_project.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.OrderModel;

public interface IOrderService {
    String submitCheckout(OrderModel orderModel , HttpServletRequest request);
    ApiResponse<?> statusPayment(HttpServletRequest request);
}
