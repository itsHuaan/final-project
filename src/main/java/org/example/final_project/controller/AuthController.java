package org.example.final_project.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.configuration.jwt.JwtProvider;
import org.example.final_project.model.*;
import org.example.final_project.service.IEmailService;
import org.example.final_project.service.IOtpService;
import org.example.final_project.service.IUserService;
import org.example.final_project.service.impl.OtpService;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Authentication")
@RestController
@RequestMapping(value = Const.API_PREFIX + "/authentication")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final IUserService userService;
    private final IEmailService emailService;
    private final IOtpService otpService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, IUserService userService, IEmailService emailService, IOtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.emailService = emailService;
        this.otpService = otpService;
    }

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

    @Operation(summary = "Verify User")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserUsingOtp(@RequestParam String otp, @RequestParam String email) {
        boolean isOtpValid = otpService.isValid(email, otp, LocalDateTime.now());
        if (isOtpValid) {
            if (userService.activateUserAccount(email) != 0) {
                otpService.setInvalid(otp, email);
                return ResponseEntity.ok("Account activated successfully!");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to activate account.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP.");
        }
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

        OtpModel otpModel = new OtpModel();
        otpModel.setOtpCode(otpService.generateOtp());
        otpModel.setEmail(credentials.getEmail());
        otpService.save(otpModel);

        EmailModel emailModel = new EmailModel(credentials.getEmail(), "OTP", otpModel.getOtpCode());
        boolean result = emailService.sendEmail(emailModel);

        int saveResult = userService.save(userModel);
        if (saveResult == 0) {
            return new ResponseEntity<>("Username or Email has been taken by another user", HttpStatus.BAD_REQUEST);
        }
        return result
                ? new ResponseEntity<>("OTP has sent to email " + credentials.getEmail(), HttpStatus.CREATED)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(summary = "Sign Users In")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest credentials) {
        if (!userService.isActivated(credentials.getUsername())) {
            return new ResponseEntity<>("This account is not activated", HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtProvider.generateTokenByUsername(userDetails.getUsername());
        return new ResponseEntity<>(new SignInResponse(
                userDetails.getUserEntity().getUserId(),
                "Bearer",
                jwt,
                userDetails.getUsername(),
                userDetails.getUser().getEmail(),
                userDetails.getRoleName()), HttpStatus.OK);
    }
}
