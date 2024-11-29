package org.example.final_project.model.enum_status;

import java.util.List;

public enum ActivateStatus {
    Active(2),
    Inactive(1),

    NotConfirmed(0);

    private final int value;

    ActivateStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean checkIfExist(int value) {
        boolean check = false;
        for (ActivateStatus e_value : ActivateStatus.values()) {
            if (value == e_value.getValue()) {
                check = true;
                break;
            }
        }
        return check;
    }
}
