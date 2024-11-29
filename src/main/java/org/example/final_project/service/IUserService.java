package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.ShopRegisterRequest;
import org.example.final_project.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService extends IBaseService<UserDto, UserModel, Long> {
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    boolean isExistingByUsernameOrEmail(String username, String email);
    boolean isActivated(String email);
    int activateUserAccount(String username, String email);
    int activateUserAccount(String email);
    int resetPassword(String email, String newPassword);
    int changePassword(String username, String oldPassword, String newPassword);
    boolean validatePassword(String email, String newPassword);
    Page<UserDto> findAllUsers(Pageable pageable);
    ResponseEntity<?> signIn(String email, String password);
    ApiResponse<?> registerForBeingShop(ShopRegisterRequest request) throws Exception;
    ApiResponse<?> acceptfromAdmin(int status , long userId) throws Exception;
    List<UserDto> findAllStatusUserBeingShop();
}
