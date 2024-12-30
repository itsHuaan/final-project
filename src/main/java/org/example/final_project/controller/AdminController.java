package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.model.LockShopRequest;
import org.example.final_project.model.ShopModel;
import org.example.final_project.service.impl.StatisticService;
import org.example.final_project.service.impl.UserService;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    UserService userService;
    StatisticService statisticService;

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

        Page<UserDto> shops = userService.getAllShop(status, PageableValidation.setDefault(pageSize, pageIndex));
        HttpStatus httpStatus = shops.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        String message = shops.isEmpty() ? "No shop fetched" : "Shop fetched";
        return ResponseEntity.status(HttpStatus.OK)
                .body(createResponse(httpStatus, message, null));
    }

    @Operation(summary = "find shop by Name or status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/find-shop-name")
    public ResponseEntity<?> getShopByName(@RequestParam(required = false) String shop_name, @RequestParam(required = false) Integer shop_status) {
        List<UserDto> userDtoList = userService.findByShopName(shop_name, shop_status);
        return ResponseEntity.ok(userDtoList);
    }

    @Operation(summary = "update shop")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/update-shop")
    public ResponseEntity<String> updateShop(@PathVariable long id, @RequestBody ShopModel shopModel) {
        return ResponseEntity.ok(userService.updateShop(id, shopModel) == 1 ? "đã update thành công" : "chưa update thành công ");
    }

    @Operation(summary = "Get statistics")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        AdminStatisticDto statisticDto = statisticService.getAdminStatisticDto();
        HttpStatus status = statisticDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        String message = statisticDto != null ? "Statistic fetched" : "No statistics found";
        return ResponseEntity.status(status).body(createResponse(status, message, statisticDto));
    }


    @Operation(summary = "Get super admin revenue")
    @GetMapping("/revenue-statistic")
    public ResponseEntity<?> getRevenue(@RequestParam int year) {
        List<RevenueStatistic> revenueStatistics = statisticService.getRevenueStatistics(year);
        HttpStatus status = !revenueStatistics.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        String message = !revenueStatistics.isEmpty() ? "Revenue fetched" : "No revenue data";
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        revenueStatistics
                )
        );
    }

    @Operation(summary = "Get statistic for shop")
    @GetMapping("/shop-statistic")
    public ResponseEntity<?> getShopStatistic() {
        ShopRatioDto shopRatio = statisticService.getShopRatioDto();
        HttpStatus status = shopRatio != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        String message = shopRatio != null ? "Shop statistic fetched" : "No statistic data";
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        shopRatio
                )
        );
    }

}
