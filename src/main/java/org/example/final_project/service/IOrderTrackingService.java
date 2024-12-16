package org.example.final_project.service;

public interface IOrderTrackingService {
    int updateStatusShipping(int status , long shopId , long orderId );
}
