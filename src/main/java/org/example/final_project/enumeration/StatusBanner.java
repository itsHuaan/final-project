package org.example.final_project.enumeration;

import lombok.Getter;

@Getter
public enum StatusBanner {
    INACTIVE(0),
    ACTIVE(1),
    OUTDATED(2),
    ExpiredBanner(3);
    private final int banner;

    StatusBanner(int banner) {
        this.banner = banner;
    }
}
