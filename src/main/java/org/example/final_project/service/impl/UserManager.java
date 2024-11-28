package org.example.final_project.service.impl;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.service.IUserManager;
import org.springframework.data.domain.Pageable;

public class UserManager implements IUserManager {
    @Override
    public ApiResponse<?> getAllUsers(Pageable pageable) {
        return null;
    }
}
