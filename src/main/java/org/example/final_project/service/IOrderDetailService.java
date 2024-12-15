package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;

public interface IOrderDetailService {
    ApiResponse<?> getOrderDetail(long userId);

    ApiResponse<?> getOrderDetailFlowShippingStatus(long userId, long shippingStatus);

    ApiResponse<?> findDetailIn4OfOrder(long userId, long oderId, long shopId);
    ApiResponse<?> findOrderInforByOrderCode(long userId , String orderCode);
}
