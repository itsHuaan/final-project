package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;

public interface IOrderDetailService {
    ApiResponse<?> getOrderDetail(long userId);
    ApiResponse<?> getOrderDetailFlowShippingStatus(long userId , long shippingStatus);
}
