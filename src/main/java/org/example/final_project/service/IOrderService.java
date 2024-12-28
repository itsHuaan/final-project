package org.example.final_project.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.OrderDto;
import org.example.final_project.model.OrderModel;

public interface IOrderService {
    String submitCheckout(OrderModel orderModel, HttpServletRequest request) throws Exception;

    ApiResponse<?> getPaymentStatus(HttpServletRequest request) throws Exception;

    ApiResponse<?> getOrdersByShopId(long shopId, Integer pageIndex, Integer pageSize, Integer statusShipping);

    String getTotalPrice(String tex);

    ApiResponse<?> getOrderTracking(Long orderId, Long shopId);

    OrderDto findByShopIdAndCodeOrder(long shopId, String orderCode);

    ApiResponse<?> checkQuatityInStock(long skuId, long currentQuatity);

    OrderModel sentNotify(HttpServletRequest request);

    ApiResponse<?> getAllUserBoughtOfThisShop(long shopId, Integer page, Integer size);

    int checkQuatity(long skuId, long currentQuatity);
    String checkOrderCode(String orderCode);
}
