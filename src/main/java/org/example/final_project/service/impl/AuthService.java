package org.example.final_project.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.configuration.jwt.JwtProvider;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.SignInResponse;
import org.example.final_project.model.*;
import org.example.final_project.service.*;
import org.example.final_project.util.EmailTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.final_project.model.validation.AuthValidation;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AuthService implements IAuthService {
    JwtProvider jwtProvider;
    IUserService userService;
    IEmailService emailService;
    IOtpService otpService;
    AuthenticationManager authenticationManager;
    ITokenBlacklistService tokenBlacklistService;

    private ApiResponse<?> createResponse(HttpStatus status, String message, Object data) {
        return new ApiResponse<>(status.value(), message, data, LocalDateTime.now());
    }

    @Override
    public ApiResponse<?> forgotPassword(String email) {
        if (!userService.isActivated(email)) {
            return createResponse(HttpStatus.BAD_REQUEST, AuthValidation.ACCOUNT_INACTIVE, null);
        }
        try {
            String jwt = jwtProvider.generateForgetPasswordToken(email);
            EmailModel emailModel = new EmailModel(email, "It seems like somebody forgot their password...", EmailTemplate.forgotPasswordEmailContent(jwt));
            emailService.sendEmail(emailModel);
            return createResponse(HttpStatus.OK, "Email sent to " + email, null);
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
        }
    }

    @Override
    public ApiResponse<?> verifyUser(String otp, String email) {
        if (!otpService.isValid(email, otp, LocalDateTime.now())) {
            return createResponse(HttpStatus.BAD_REQUEST, "Invalid OTP.", null);
        }
        if (userService.activateUserAccount(email) == 0) {
            return createResponse(HttpStatus.BAD_REQUEST, "Failed to activate account.", null);
        }
        otpService.setInvalid(otp, email);
        return createResponse(HttpStatus.OK, "Account activated successfully.", null);
    }

    @Override
    public ApiResponse<?> logOut(String token) {
        try {
            Claims claims = jwtProvider.parseJwt(token);
            claims.setExpiration(new Date());
            return createResponse(HttpStatus.OK, "Logged out successfully", null);
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during logout", null);
        }
    }

    @Override
    public ApiResponse<?> signIn(SignInRequest credentials) {
        if (!userService.isActivated(credentials.getEmail())) {
            return createResponse(HttpStatus.FORBIDDEN, AuthValidation.ACCOUNT_INACTIVE, null);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            return createResponse(HttpStatus.UNAUTHORIZED, AuthValidation.BAD_CREDENTIAL, null);
        } catch (LockedException e) {
            return createResponse(HttpStatus.LOCKED, AuthValidation.ACCOUNT_LOCKED, null);
        } catch (DisabledException e) {
            return createResponse(HttpStatus.FORBIDDEN, AuthValidation.ACCOUNT_INACTIVE, null);
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", null);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtProvider.generateTokenByEmail(userDetails.getUser().getEmail());
        SignInResponse response = new SignInResponse(
                userDetails.getUserEntity().getUserId(),
                "Bearer",
                jwt,
                userDetails.getUsername(),
                userDetails.getUser().getEmail(),
                userDetails.getRoleName());

        return createResponse(HttpStatus.OK, "Logged In", response);
    }

    @Override
    public ApiResponse<?> signUp(SignUpRequest credentials) {
        if (userService.isExistingByUsernameOrEmail(credentials.getUsername(), credentials.getEmail())) {
            return createResponse(HttpStatus.BAD_REQUEST, "Username or email is already in use", null);
        }

        UserModel userModel = new UserModel();
        userModel.setName(credentials.getName() != null ? credentials.getName() : credentials.getUsername());
        userModel.setEmail(credentials.getEmail());
        userModel.setPassword(credentials.getPassword());
        userModel.setUsername(credentials.getUsername());
        userModel.setRoleId(credentials.getRoleId());

        OtpModel otpModel = new OtpModel();
        otpModel.setOtpCode(otpService.generateOtp());
        otpModel.setEmail(credentials.getEmail());

        try {
            otpService.save(otpModel);
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save OTP", null);
        }

        EmailModel emailModel = new EmailModel(credentials.getEmail(), "OTP", EmailTemplate.otpEmailContent(otpModel.getOtpCode()));
        emailService.sendEmail(emailModel);

        if (userService.save(userModel) == 0) {
            return createResponse(HttpStatus.BAD_REQUEST, AuthValidation.ACCOUNT_CONFLICT, null);
        }

        return createResponse(HttpStatus.OK, "An OTP was sent to email " + credentials.getEmail() + ".", null);
    }


    @Override
    public ApiResponse<?> resetPassword(String token, String newPassword) {
        if (tokenBlacklistService.isTokenPresent(token)) {
            return createResponse(HttpStatus.BAD_REQUEST, AuthValidation.TOKEN_INVALID, null);
        }
        Claims claims = jwtProvider.parseJwt(token);
        if (claims.getExpiration().before(new Date())) {
            return createResponse(HttpStatus.BAD_REQUEST, AuthValidation.TOKEN_EXPIRED, null);
        }
        String email = claims.getSubject();
        if (!userService.isActivated(email)) {
            return createResponse(HttpStatus.BAD_REQUEST, AuthValidation.ACCOUNT_INACTIVE, null);
        }
        int changePassword = userService.resetPassword(email, newPassword);
        if (changePassword != 1) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Password reset failed", null);
        }
        tokenBlacklistService.saveToken(token);
        return createResponse(HttpStatus.OK, "Password reset successfully", null);
    }
}
