package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.CancelOrderModel;

public interface IOrderDetailService {
    ApiResponse<?> getOrderDetail(long userId);

    ApiResponse<?> getOrderDetailByShippingStatus(long userId, long shippingStatus);

    ApiResponse<?> findOrderDetailInfo(long userId, long oderId, long shopId);

    ApiResponse<?> findOrderInfoByOrderCode(long userId, String orderCode);

    int updateFeedbackStatus(long orderDetailId);

    ApiResponse<?> cancelOrder(CancelOrderModel cancelOrderModel);
}
