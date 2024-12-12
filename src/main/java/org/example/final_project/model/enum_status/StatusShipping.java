package org.example.final_project.model.enum_status;

public enum StatusShipping {
    Create(0), //mới tạo
    Pending(1), // chờ xử lý
    Confirmed(2), // đã xác nhạn
    Shipping_pending(3),
    Shipping_confirmed(4),
    Delivering(5),
    Delivered(6),
    Paid(7),
    Completed(8),
    Cancelled(9);
    private int shipStatus;

    StatusShipping(int StatusShipping) {
        this.shipStatus = StatusShipping;
    }
    public int getStatus() {
        return shipStatus;
    }
}
