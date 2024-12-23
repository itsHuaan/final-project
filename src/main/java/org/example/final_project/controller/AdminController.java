package org.example.final_project.controller;

import com.cloudinary.api.exceptions.BadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.UserDto;
import org.example.final_project.model.LockShopRequest;
import org.example.final_project.model.ShopModel;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "ADMIN")
@RestController
@RequestMapping(Const.API_PREFIX + "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @Operation(summary = "Admin approves store status ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{userId}/switching-status-for-shop")
    public ResponseEntity<ApiResponse<?>> statusOfShop(@PathVariable long userId,
                                                       @RequestBody LockShopRequest request) {
        try {
            ApiResponse<?> response = userService.acceptFromAdmin(userId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Get all shop")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/shop")
    public ResponseEntity<?> getAllShop(@RequestParam(defaultValue = "0") Integer status,
                                        @RequestParam(required = false) Integer pageIndex,
                                        @RequestParam(required = false) Integer pageSize) {
        try {
            Page<UserDto> userDtoList = userService.getAllShop(status, pageIndex, pageSize);
            return !userDtoList.isEmpty()
                    ? ResponseEntity.ok(createResponse(HttpStatus.OK, "Shops fetched successfully", userDtoList))
                    : ResponseEntity.ok(createResponse(HttpStatus.OK, "No shops found", null));
        } catch (BadRequest e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", null));
        }
    }
    @Operation(summary = "find shop by Name or status")
    @GetMapping("/find-shop-name")
    public ResponseEntity<?> getShopByName(@RequestParam(required = false) String shop_name , @RequestParam(required = false) Integer shop_status ) {
        List<UserDto> userDtoList = userService.findByShopName(shop_name,shop_status);
        return ResponseEntity.ok(userDtoList);
    }
    @Operation(summary = "update shop")
    @PutMapping("/{id}/update-shop")
    public ResponseEntity<String> updateShop(@PathVariable long id, @RequestBody ShopModel shopModel) {
        return ResponseEntity.ok(userService.updateShop(id,shopModel) ==1 ? "đã update thành công" : "chưa update thành công ");
    }



}
