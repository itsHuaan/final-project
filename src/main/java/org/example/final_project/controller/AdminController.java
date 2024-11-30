package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.UserDto;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "ADMIN")
@RestController
@RequestMapping(Const.API_PREFIX+"/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    @Operation(summary = "Admin approves store status ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{userid}/createShop")
    public ResponseEntity<ApiResponse<?>> createShop(@PathVariable long userid, @RequestParam("status") int status ) {
        try {
            ApiResponse<?> response = userService.acceptFromAdmin(status,userid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null,LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get All SHop Flow STATUS 1 2 3")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-status-shop")
    public ResponseEntity<List<UserDto>> getStatusShop() {
        List<UserDto> userDtoList = userService.findAllStatusUserBeingShop();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    }
    @Operation(summary = "Get All SHop Flow STATUS 1 2 3 AND PAGEABLE")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-status-shop/page")
    public ResponseEntity<?> getStatusShop(@RequestParam int page,
                                                       @RequestParam int size) {
        try {
            Page<UserDto> userDtoList = userService.findAllStatusUserBeingShop(page,size);
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
}
