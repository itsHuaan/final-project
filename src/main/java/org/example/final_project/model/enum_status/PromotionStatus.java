package org.example.final_project.model.enum_status;

public enum PromotionStatus {
    COMING_SOON(0),
    ACTIVE(1),
    ENDED(2),
    REJECTED(3);

    private final int value;

    PromotionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
