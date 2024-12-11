package org.example.final_project.model.enum_status;

public enum CheckoutStatus {
    Pending(1),
    Completed (2),
    Failed(3),
    Canceled(4);




    private int checkout;

    CheckoutStatus(int CheckoutStatus) {
        this.checkout = CheckoutStatus;
    }
    public int getStatus() {
        return checkout;
    }
}
