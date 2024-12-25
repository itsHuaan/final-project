package org.example.final_project.enumeration;

import lombok.Getter;

@Getter
public enum CheckoutStatus {
    PENDING(1),
    COMPLETED(2),
    FAILED(3),
    CANCELED(4);

    private final int value;

    CheckoutStatus(int CheckoutStatus) {
        this.value = CheckoutStatus;
    }
}
