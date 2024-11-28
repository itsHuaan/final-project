package org.example.final_project.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.configuration.jwt.JwtProvider;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.SignInResponse;
import org.example.final_project.model.*;
import org.example.final_project.service.IAuthService;
import org.example.final_project.service.IEmailService;
import org.example.final_project.service.IOtpService;
import org.example.final_project.service.IUserService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/authentication")
public class AuthController {
    IAuthService authService;


    /*
     @Operation(summary = "Verify User With Token")
     @GetMapping("/verify/{token}")
     public ResponseEntity<?> verifyUserWithToken(@PathVariable String token) {
         try {
             String username = jwtProvider.getKeyByValueFromJWT("username", token);
             String email = jwtProvider.getKeyByValueFromJWT("email", token);
             if (userService.activateUserAccount(username, email) != 0) {
                 return ResponseEntity.ok("Account activated successfully!");
             } else {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to activate account.");
             }
         } catch (ExpiredJwtException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
         }
     }


     @Operation(summary = "Send OTP To User")
     @GetMapping("/send-otp")
     public ResponseEntity<?> sendOtp(@RequestParam String recipient) {

     }

     @Operation(summary = "Sign Users Up")
     @PostMapping("/sign-up")
     public ResponseEntity<?> signUp(@RequestBody SignUpRequest credentials) {
         UserModel userModel = new UserModel();
         userModel.setName(credentials.getName());
         userModel.setEmail(credentials.getEmail());
         userModel.setPassword(credentials.getPassword());
         userModel.setUsername(credentials.getUsername());
         userModel.setRoleId(credentials.getRoleId());
         int result = userService.save(userModel);
         String jwt = jwtProvider.generateTokenByUsername(credentials.getUsername());
         return result != 0
                 ? new ResponseEntity<>(jwt, HttpStatus.CREATED)
                 : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
     */
    @Operation(summary = "Send forgot password email")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody SendForgotPasswordEmailRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request.getEmail()));
    }

    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(token, request.getNewPassword()));
    }

    @Operation(summary = "Verify User")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserUsingOtp(@RequestParam String otp, @RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyUser(otp, email));
    }


    @Operation(summary = "Sign Users Up")
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody SignUpRequest credentials) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(credentials));
    }

    @Operation(summary = "Sign Users In")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest credentials) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signIn(credentials));
    }

    @Operation(summary = "Log Out")
    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestParam String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.logOut(token));
    }
}
