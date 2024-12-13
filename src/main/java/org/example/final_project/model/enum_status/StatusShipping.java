package org.example.final_project.model.enum_status;

public enum StatusShipping {
    Create(0), //mới tạo
    Pending(1), // chờ xử lý
    Confirmed(2), // đã xác nhạn
    Shipping_pending(3), // chờ vận chuyển
    Shipping_confirmed(4), // đã xacs nhân vận chuyển
    Delivering(5), // đang giao hàng
    Delivered(6), // Đã giao hàng
    Paid(7), // Đã thanh toán
    Completed(8), // Thành công
    Cancelled(9);//Hủy
    private int shipStatus;

    StatusShipping(int StatusShipping) {
        this.shipStatus = StatusShipping;
    }
    public int getStatus() {
        return shipStatus;
    }
}
