package org.example.final_project.util;

public enum STATUS {
    ACTIVE(1),
    INACTIVE(0);
    private int status;

    STATUS(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
