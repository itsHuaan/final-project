package org.example.final_project.service;

import org.example.final_project.dto.UserDto;
import org.example.final_project.model.UserModel;

public interface IUserService extends IBaseService<UserDto, UserModel, Long> {
    UserDto findByUsername(String username);
    boolean isExistingByUsernameOrEmail(String username, String email);
    boolean isActivated(String username);
    int activateUserAccount(String username, String email);
}
