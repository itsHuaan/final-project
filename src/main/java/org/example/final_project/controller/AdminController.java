package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.ShopRegisterRequest;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Tag(name = "ADMIN")
@RestController
@RequestMapping(Const.API_PREFIX+"/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userid}/createShop")
    public ResponseEntity<ApiResponse<?>> createShop(@PathVariable long userid, @RequestParam("status") int status ) {
        try {
            ApiResponse<?> response = userService.acceptfromAdmin(status,userid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null,LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
