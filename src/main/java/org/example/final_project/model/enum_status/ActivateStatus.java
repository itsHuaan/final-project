package org.example.final_project.model.enum_status;

public enum ActivateStatus {
    Active(1),
    Inactive(0);
    private final int value;

    ActivateStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
