package org.example.final_project.enumeration;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE(1),
    INACTIVE(2),
    REJECTED(0);

    private final int value;

    ProductStatus(int value) {
        this.value = value;
    }

    public boolean checkIfExist(int value) {
        boolean check = false;
        for (ProductStatus e_value : ProductStatus.values()) {
            if (value == e_value.getValue()) {
                check = true;
                break;
            }
        }
        return check;
    }
}
