package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.ChangePasswordRequest;
import org.example.final_project.model.SignInRequest;
import org.example.final_project.model.SignUpRequest;

public interface IAuthService {
    ApiResponse<?> forgotPassword(String email);

    ApiResponse<?> signIn(SignInRequest credentials);

    ApiResponse<?> signUp(SignUpRequest credentials);

    ApiResponse<?> resetPassword(String token, String newPassword);

    ApiResponse<?> verifyUser(String otp, String email);

    ApiResponse<?> logOut(String token);

    ApiResponse<?> changePassword(String username, ChangePasswordRequest request);

    ApiResponse<?> validatePassword(String username, String password);

    ApiResponse<?> sendOtp(String email);
}
