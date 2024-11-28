package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface IUserManager {
    ApiResponse<?> getAllUsers(Pageable pageable);
}
