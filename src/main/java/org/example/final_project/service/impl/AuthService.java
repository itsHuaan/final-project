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

import static org.example.final_project.dto.ApiResponse.*;
import static org.example.final_project.util.Const.EMAIL_PATTERN;

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

    @Override
    public ApiResponse<?> forgotPassword(String email) {
        if (!userService.isActivated(email)) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_INACTIVE);
        }
        try {
            String jwt = jwtProvider.generateForgetPasswordToken(email);
            EmailModel emailModel = new EmailModel(email, "It seems like somebody forgot their password...", EmailTemplate.forgotPasswordEmailContent(jwt));
            emailService.sendEmail(emailModel);
            return createResponse(HttpStatus.OK, "Email sent to " + email, jwt);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while sending the email");
        }
    }

    @Override
    public ApiResponse<?> verifyUser(String otp, String email) {
        if (!otpService.isValid(email, otp, LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid OTP.");
        }
        if (userService.activateUserAccount(email) == 0) {
            throw new IllegalStateException("Failed to activate account.");
        }
        otpService.setInvalid(otp, email);
        return createResponse(HttpStatus.OK, "Account activated successfully.", null);
    }

    @Override
    public ApiResponse<?> logOut(String token) {
        try {
            if (tokenBlacklistService.isTokenPresent(token)) {
                tokenBlacklistService.saveToken(token);
            }
            return createResponse(HttpStatus.OK, "Logged out successfully", null);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during logout");
        }
    }

    @Override
    public ApiResponse<?> changePassword(String username, ChangePasswordRequest request) {
        if (userService.changePassword(username, request.getOldPassword(), request.getNewPassword()) == 1){
            return createResponse(HttpStatus.OK, "Password changed successfully.", null);
        }
        if (userService.changePassword(username, request.getOldPassword(), request.getNewPassword()) == -1){
            throw new IllegalStateException(AuthValidation.ACCOUNT_UNAVAILABLE);
        } else {
            throw new IllegalArgumentException("Incorrect old password.");
        }
    }

    @Override
    public ApiResponse<?> validatePassword(String username, String password) {
        return null;
    }

    @Override
    public ApiResponse<?> signIn(SignInRequest credentials) {
        if (!userService.isActivated(credentials.getEmail())) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_INACTIVE);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new SecurityException(AuthValidation.BAD_CREDENTIAL);
        } catch (LockedException e) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_LOCKED);
        } catch (DisabledException e) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_INACTIVE);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during authentication");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtProvider.generateTokenByEmail(userDetails.getUser().getEmail());

        SignInResponse response = new SignInResponse(
                userDetails.getUserEntity().getUserId(),
                "Bearer",
                jwt,
                userDetails.getUsername(),
                userDetails.getUserEntity().getName(),
                userDetails.getUser().getEmail(),
                userDetails.getRoleName()
        );

        return createResponse(HttpStatus.OK, "Logged In", response);
    }

    @Override
    public ApiResponse<?> sendOtp(String email) {
        OtpModel otpModel = new OtpModel();
        otpModel.setOtpCode(otpService.generateOtp());
        otpModel.setEmail(email);

        try {
            otpService.save(otpModel);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save OTP");
        }

        EmailModel emailModel = new EmailModel(email, "OTP", EmailTemplate.otpEmailContent(otpModel.getOtpCode()));
        return emailService.sendEmail(emailModel)
                ? createResponse(HttpStatus.OK, "OTP confirmation email sent to " + email, null)
                : createResponse(HttpStatus.BAD_REQUEST, "Email not sent", null);
    }

    @Override
    public ApiResponse<?> signUp(SignUpRequest credentials) {
        if (!EMAIL_PATTERN.matcher(credentials.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        UserModel userModel = new UserModel();
        userModel.setName(credentials.getName() != null ? credentials.getName() : credentials.getUsername());
        userModel.setEmail(credentials.getEmail());
        userModel.setPassword(credentials.getPassword());
        userModel.setUsername(credentials.getUsername());

        if (userService.save(userModel) == 0) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_CONFLICT);
        }

        ApiResponse<?> response = sendOtp(credentials.getEmail());
        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new IllegalStateException(response.getMessage());
        }

        return createResponse(HttpStatus.OK, "An OTP was sent to email " + credentials.getEmail() + ".", null);
    }

    @Override
    public ApiResponse<?> resetPassword(String token, String newPassword) {
        if (AuthValidation.tokenValidator(token)) {
            throw new SecurityException(AuthValidation.TOKEN_INVALID);
        }
        if (tokenBlacklistService.isTokenPresent(token)) {
            throw new IllegalArgumentException(AuthValidation.TOKEN_INVALID);
        }
        Claims claims = jwtProvider.parseJwt(token);
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalArgumentException(AuthValidation.TOKEN_EXPIRED);
        }
        String email = claims.getSubject();
        if (!userService.isActivated(email)) {
            throw new IllegalStateException(AuthValidation.ACCOUNT_INACTIVE);
        }
        if (userService.resetPassword(email, newPassword) != 1) {
            throw new RuntimeException("Password reset failed");
        } else {
            tokenBlacklistService.saveToken(token);
            return createResponse(HttpStatus.OK, "Password reset successfully", null);
        }
    }
}
