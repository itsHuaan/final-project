package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.ChatUserDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService extends IBaseService<UserDto, UserModel, Long> {
    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    boolean isExistingByUsernameOrEmail(String username, String email);

    boolean isActivated(String email);

    int activateUserAccount(String email);

    int resetPassword(String email, String newPassword);

    int changePassword(String username, String oldPassword, String newPassword);

    boolean validatePassword(String email, String password);

    Page<UserDto> findAllUsers(Pageable pageable, String name, Integer status);

    ApiResponse<?> registerForBeingShop(ShopRegisterRequest request) throws Exception;

    ApiResponse<?> acceptFromAdmin(long userId, LockShopRequest request) throws Exception;

    ResponseEntity<?> updateProfile(String username, ProfileUpdateRequest request);

    ResponseEntity<?> changeAccountStatus(long userId, ChangeAccountStatusRequest request);

    Page<UserDto> getAllShop(Integer status, Pageable pageable);

    List<UserDto> findActiveUsers();

    List<UserDto> findByShopName(String shopName, Integer shopStatus);

    int updateShop(Long userId, ShopModel shopModel);

    List<ChatUserDto> getChatUsers(Long senderId);
}
