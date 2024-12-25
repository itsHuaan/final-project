package org.example.final_project.enumeration;

import lombok.Getter;

@Getter
public enum ShopStatus {
    ACTIVE(1),
    PENDING(2),
    REJECTED(3),
    LOCKED(4),
    INACTIVE(0);

    private final int value;

    ShopStatus(int value) {
        this.value = value;
    }
}
