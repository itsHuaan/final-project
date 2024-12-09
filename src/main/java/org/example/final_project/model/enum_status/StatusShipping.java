package org.example.final_project.model.enum_status;

public enum StatusShipping {
    Pending(1),
    Processing(2),
    Shipped(3),
    InTransit(4),
    Delivered(5),
    DeliveryFailed(6),
    Returned(7);



    private int shipStatus;

    StatusShipping(int StatusShipping) {
        this.shipStatus = StatusShipping;
    }
    public int getStatus() {
        return shipStatus;
    }
}
