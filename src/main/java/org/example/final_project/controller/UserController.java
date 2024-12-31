package org.example.final_project.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.*;
import org.example.final_project.service.*;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/user")
public class UserController {
    IUserService userService;
    IAuthService authService;
    IAddressService addressService;
    IShippingAddressService shippingAddressService;
    IOrderDetailService orderDetailService;

    @Operation(summary = "Get all user")
    @GetMapping
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "0") Integer pageIndex,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Integer status) {
        Pageable pageable = PageableValidation.setDefault(pageIndex, pageSize);
        Page<UserDto> result = userService.findAllUsers(pageable, name, status);
        return result.hasContent()
                ? ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "User fetched",
                        result
                )
        )
                : ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.NO_CONTENT,
                        "No user found",
                        result
                )
        );
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDto result = userService.getById(id);
        return result != null ? ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "User found",
                result))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(
                HttpStatus.NOT_FOUND,
                "User not found",
                null));
    }

    @Operation(summary = "Create Shop")
    @PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_SELLER')")
    @PostMapping("/register-shop")
    public ResponseEntity<?> registerForBeingShop(ShopRegisterRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.registerForBeingShop(request));
        } catch (HttpClientErrorException.Conflict e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(createResponse(HttpStatus.CONFLICT, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }

    @Operation(summary = "Delete an user")
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
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
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
    public ResponseEntity<?> updateProfile(ProfileUpdateRequest request) {
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

    @Operation(summary = "Add shipping address for user, maximum = 20 addresses")
    @PostMapping("/{id}/add-address")
    public ResponseEntity<?> selectAddress(
            @PathVariable Long id,
            @RequestBody AddShippingAddressRequest request) {
        HttpStatus httpStatus;
        String message;
        try {
            int result = shippingAddressService.addAddress(id, request);
            httpStatus = result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            message = result == 1 ? "Added " + String.join(", ", addressService.findAddressNamesFromParentId(request.getAddressId())) : "Failed to add shipping address";
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = e.getMessage();
        }
        return ResponseEntity.status(httpStatus)
                .body(createResponse(
                        httpStatus,
                        message,
                        null)
                );
    }

    @Operation(summary = "Update an existing shipping address")
    @PutMapping("/{id}/update-address")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long id,
            @RequestBody UpdateShippingAddressRequest request) {
        HttpStatus httpStatus;
        String message;
        try {
            int result = shippingAddressService.updateAddress(id, request);
            httpStatus = result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            message = result == 1 ? "Update shipping address for user " + userService.getById(id).getUsername() : "Failed to update shipping address";
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = e.getMessage();
        }
        return ResponseEntity.status(httpStatus)
                .body(createResponse(
                        httpStatus,
                        message,
                        null)
                );
    }

    @Operation(summary = "Delete an existing shipping address")
    @DeleteMapping("/{id}/delete-address")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long id,
            @RequestParam Long shippingAddressId) {
        HttpStatus httpStatus;
        String message;
        try {
            int result = shippingAddressService.deleteAddress(id, shippingAddressId);
            httpStatus = result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            message = result == 1 ? "Deleted shipping address for user " + userService.getById(id).getUsername() : "Failed to delete shipping address";
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = e.getMessage();
        }
        return ResponseEntity.status(httpStatus)
                .body(createResponse(
                        httpStatus,
                        message,
                        null)
                );
    }

    @Operation(summary = "Get all shipping addresses of an user")
    @GetMapping("/{id}/shipping-address")
    public ResponseEntity<?> getShippingAddress(@PathVariable Long id) {
        try {
            List<ShippingAddressDto> result = shippingAddressService.getShippingAddresses(id);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(HttpStatus.OK,
                    "All shipping addresses fetch",
                    result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(HttpStatus.BAD_REQUEST,
                    "No shipping addresses fetch",
                    null));
        }
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public UserDto addUser(@Payload Long userId) {
        return userService.getById(userId);
    }

    @GetMapping("/active-users")
    public ResponseEntity<?> findConnectedUsers() {
        return ResponseEntity.ok(userService.findActiveUsers());
    }

}

