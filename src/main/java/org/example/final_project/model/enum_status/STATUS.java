package org.example.final_project.model.enum_status;

public enum STATUS {
    ACTIVE(1),
    WAIT(2),
    REFUSE(3),
    LOCKED(4),
    INACTIVE(0);

    private int status;

    STATUS(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
