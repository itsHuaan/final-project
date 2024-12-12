package org.example.final_project.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.model.OrderModel;
import org.springframework.data.domain.Page;

public interface IOrderService {
    String submitCheckout(OrderModel orderModel , HttpServletRequest request) throws Exception;
    ApiResponse<?> statusPayment(HttpServletRequest request) throws Exception;
    Page<OrderDto> getAllOrderByShopId(long shopId , Integer pageIndex , Integer pageSize );
    String getTotalPrice(String tex);
    ApiResponse<?> getOrderTracking(Long orderId , Long shopId);
    OrderDto findByShopIdAndCodeOrder(long shopId , String orderCode);
}
