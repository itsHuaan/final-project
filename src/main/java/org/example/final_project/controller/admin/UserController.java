package org.example.final_project.controller.admin;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.ShopRegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.UserDto;
import org.example.final_project.service.IUserService;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/user")
public class UserController {
    IUserService userService;

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
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched",
                result,
                LocalDateTime.now()));
    }
    @PostMapping("/register_shop")
    @PreAuthorize("hasRole('ROLE_BUYER') or hasRole('ROLE_SELLER')")
    public ResponseEntity<?> becomeShop(@RequestBody ShopRegisterRequest shopRegisterRequest) {
        ApiResponse<?> response = userService.registerForBeingShop(shopRegisterRequest);
        return ResponseEntity.ok(response);
    }






}

