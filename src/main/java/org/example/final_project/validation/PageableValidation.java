package org.example.final_project.validation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableValidation {
    public static Pageable setDefault(Integer pageSize, Integer pageIndex) {
        if (pageSize == null || pageIndex == null) {
            return Pageable.unpaged();
        }
        if (pageSize > 0 && pageIndex >= 0) {
            return PageRequest.of(pageIndex, pageSize);
        }
        return Pageable.unpaged();
    }
}
