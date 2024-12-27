package org.example.final_project.enumeration;

import lombok.Getter;

@Getter
public enum ShippingStatus {
    CREATED(0), //mới tạo
    PENDING(1), // chờ xử lý
    CONFIRMED(2), // đã xác nhạn
    PENDING_SHIPPING(3), // chờ vận chuyển
    CONFIRMED_SHIPPING(4), // đã xacs nhân vận chuyển
    DELIVERING(5), // đang giao hàng
    DELIVERED(6), // Đã giao hàng
    PAID(7), // Đã thanh toán
    COMPLETED(8), // Thành công
    CANCELLED(9),//Hủy
    NotReceive(10);
    private final int value;

    ShippingStatus(int StatusShipping) {
        this.value = StatusShipping;
    }
}
