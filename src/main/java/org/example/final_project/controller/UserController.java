package org.example.final_project.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.dto.UserDto;
import org.example.final_project.dto.ApiResponse;

import org.example.final_project.model.ChangeAccountStatusRequest;
import org.example.final_project.model.ChangePasswordRequest;
import org.example.final_project.model.ProfileUpdateRequest;
import org.example.final_project.model.ShopRegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.IAddressService;
import org.example.final_project.service.IAuthService;
import org.example.final_project.service.IUserService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import static org.example.final_project.dto.ApiResponse.*;

import java.time.LocalDateTime;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/user")
public class UserController {
    IUserService userService;
    IAuthService authService;
    IAddressService addressService;

    @GetMapping
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "0") Integer pageIndex,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<UserDto> result = userService.findAllUsers(PageRequest.of(pageIndex, pageSize));
        return result.hasContent()
                ? ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched",
                result,
                LocalDateTime.now()))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDto result = userService.getById(id);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Fetched",
                result))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(
                HttpStatus.NOT_FOUND,
                "User not found",
                null));
    }

    @Operation(summary = "Create Shop")
    @PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_SELLER')")
    @PostMapping("/register-shop")
    public ResponseEntity<ApiResponse<?>> registerForBeingShop(@ModelAttribute ShopRegisterRequest request) {
        try {
            ApiResponse<?> response = userService.registerForBeingShop(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HttpClientErrorException.Conflict e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        int deleteResult = userService.delete(id);

        return deleteResult == 1
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(HttpStatus.OK, "Deleted user has id " + id, null))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                createResponse(HttpStatus.NOT_FOUND, "User not found", null));
    }

    @Operation(summary = "Update password")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            String username = userDetails.getUsername();
            try {
                ApiResponse<?> response = authService.changePassword(username, request);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponse(HttpStatus.UNAUTHORIZED,
                                e.getMessage(),
                                null));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createResponse(HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                null));
            }
        } else {
            return new ResponseEntity<>("Unable to retrieve user information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update profile")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@ModelAttribute ProfileUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            String username = userDetails.getUsername();
            try {
                return userService.updateProfile(username, request);
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null));
            }
        } else {
            return new ResponseEntity<>("Unable to retrieve user information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Activate/Deactivate user account")
    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeUserStatus(@RequestBody ChangeAccountStatusRequest request, @PathVariable Long id) {
        return userService.changeAccountStatus(id, request);
    }

    @PostMapping("/{id}/add-address")
    public ResponseEntity<?> selectAddress(
            @PathVariable Long id,
            @RequestParam Long addressId) {
        userService.addAddress(id, addressId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createResponse(HttpStatus.OK,
                        "Added " + String.join("/", addressService.findAddressNamesFromParentId(addressId)),
                        null));
    }
}

