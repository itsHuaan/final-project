package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.*;
import org.example.final_project.service.IAuthService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/authentication")
public class AuthController {
    IAuthService authService;

    @Operation(summary = "Send forgot password email")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody SendForgotPasswordEmailRequest request) {
        try {
            return ResponseEntity.ok(authService.forgotPassword(request.getEmail()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(HttpStatus.CONFLICT, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
        try {
            return ResponseEntity.ok(authService.resetPassword(token, request.getNewPassword()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(HttpStatus.CONFLICT, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Verify User")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserUsingOtp(@RequestParam String otp, @RequestParam String email) {
        try {
            return ResponseEntity.ok(authService.verifyUser(otp, email));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(HttpStatus.CONFLICT, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Sign Users Up")
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody SignUpRequest credentials) {
        try {
            return ResponseEntity.ok(authService.signUp(credentials));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Sign Users In")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest credentials) {
        try {
            return ResponseEntity.ok(authService.signIn(credentials));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(HttpStatus.CONFLICT, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Log Out")
    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestParam String token) {
        try {
            return ResponseEntity.ok(authService.logOut(token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null));
        }
    }

    @Operation(summary = "Resend OTP")
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody SendForgotPasswordEmailRequest request) {
        try{
            return ResponseEntity.ok(authService.sendOtp(request.getEmail()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
        }
    }
}
