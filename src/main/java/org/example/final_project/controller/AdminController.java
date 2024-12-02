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
    @PostMapping("/{userid}/switching-status-for-shop")
    public ResponseEntity<ApiResponse<?>> statusOfShop(@PathVariable long userid, @RequestParam("status") int status ) {
        try {
            ApiResponse<?> response = userService.acceptFromAdmin(status,userid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null,LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get All Shop Refuse Wait ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-shop-waited")
    public ResponseEntity<List<UserDto>> getStatusShopWaited() {
        List<UserDto> userDtoList = userService.findAllStatusWaited();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    }
    @Operation(summary = "Get All SHop Flow STATUS Wait AND PAGEABLE")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-shop-waited/page")
    public ResponseEntity<?> getStatusShopWaited(@RequestParam int page,
                                                       @RequestParam int size) {
        try {
            Page<UserDto> userDtoList = userService.findAllStatusWaitedPage(page,size);
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }
    @Operation(summary = "Find All SHop By Status Page  ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/find-shop-status/page")
    public ResponseEntity<?> findAllStatusPageShop(@RequestParam("status") int status  ,
            @RequestParam int page,
                                           @RequestParam int size) {
        try {
            Page<UserDto> userDtoList = userService.findAllShopByPageStatus(status, page,size);
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Find All SHop By Status  ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/find-shop-status")
    public ResponseEntity<?> findAllStatusShop(@RequestParam("status") int status){

            List<UserDto> userDtoList = userService.findAllShopByStatus(status);
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);

    }
    @Operation(summary = "Get All SHop By Status  ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-shop-status")
    public ResponseEntity<?> getAllStatusShop() {
            List<UserDto> userDtoList = userService.getAllShopStatus();
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }
    @Operation(summary = "Get All SHop By Status Page  ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-shop-status/page")
    public ResponseEntity<?> getAllStatusPageShop(@RequestParam int page,
                                                  @RequestParam int size)  {
        try {
            Page<UserDto> userDtoList = userService.getAllShopStatusPage( page,size);
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>( e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

}
