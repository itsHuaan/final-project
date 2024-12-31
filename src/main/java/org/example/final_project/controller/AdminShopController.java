package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.CartSkuDto;
import org.example.final_project.dto.PeriodicStatisticDto;
import org.example.final_project.dto.RevenueStatistic;
import org.example.final_project.dto.ShopStatisticDto;
import org.example.final_project.validation.PageableValidation;
import org.example.final_project.service.IOrderService;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;
import static org.example.final_project.util.Const.*;

@Slf4j
@Tag(name = "ADMIN SHOP")
@RestController
@RequestMapping(Const.API_PREFIX + "/shop")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminShopController {
    IOrderService orderService;
    IStatisticService statisticService;


    @GetMapping("/{shopId}/detail-order")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> getShopDetail(@PathVariable Long shopId, @RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderTracking(orderId, shopId));
    }

    @GetMapping("/{shopId}/order")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> getOrder(@PathVariable long shopId, @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer statusShip) {
        return ResponseEntity.ok(orderService.getOrdersByShopId(shopId, page, size, statusShip));
    }

    @GetMapping("/{shopId}/find-order")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> findOrder(@PathVariable Long shopId, @RequestParam String orderCode) {
        try {
            return ResponseEntity.ok(orderService.findByShopIdAndCodeOrder(shopId, orderCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Order");
        }
    }

    @GetMapping("/{shopId}/periodic-statistics")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<?> getPeriodicStatistics(@PathVariable Long shopId) {
        List<PeriodicStatisticDto> periodicStatistics = statisticService.getPeriodicStatistics(shopId);
        return periodicStatistics != null
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Statistic fetched",
                        periodicStatistics
                )
        )
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                createResponse(
                        HttpStatus.NOT_FOUND,
                        "Statistic not found",
                        null
                )
        );
    }

    @GetMapping("/{shopId}/low-stock")
    public ResponseEntity<?> getLowStock(@PathVariable Long shopId,
                                         @RequestParam(required = false) Integer quantity,
                                         @RequestParam(required = false) Integer pageSize,
                                         @RequestParam(required = false) Integer pageIndex) {
        Pageable pageable = PageableValidation.setDefault(pageSize, pageIndex);
        Page<CartSkuDto> lowStockProducts = statisticService.getLowStockProducts(shopId, quantity == null || quantity <= 0 ? 100 : quantity, pageable);
        HttpStatus status = !lowStockProducts.isEmpty() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        String message = !lowStockProducts.isEmpty() ? "Low stock products fetched" : "No products fetched";
        return ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        status,
                        message,
                        lowStockProducts
                )
        );
    }

    @GetMapping("/{shopId}/revenue-statistic")
    public ResponseEntity<?> getRevenue(@PathVariable Long shopId,
                                         @RequestParam int year) {
        List<RevenueStatistic> revenueStatistics = statisticService.getRevenueStatistics(shopId, year);
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

    @GetMapping("/{shopId}/statistics")
    public ResponseEntity<?> getStatistic(@PathVariable Long shopId,
                                          @Parameter(description = "today/week/month/year/custom") @RequestParam String period,
                                          @RequestParam(required = false) LocalDate startDate,
                                          @RequestParam(required = false) LocalDate endDate) {
        ShopStatisticDto statistics;
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Statistic fetched";
        switch (period.toLowerCase()) {
            case "today":
                statistics = statisticService.getStatistics(shopId, START_OF_DAY, END_OF_DAY);
                break;
            case "week":
                statistics = statisticService.getStatistics(shopId, START_OF_WEEK, END_OF_DAY);
                break;
            case "month":
                statistics = statisticService.getStatistics(shopId, START_OF_MONTH, END_OF_DAY);
                break;
            case "year":
                statistics = statisticService.getStatistics(shopId, START_OF_YEAR, END_OF_DAY);
                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    httpStatus = HttpStatus.BAD_REQUEST;
                    message = "Start time and end time are null";
                    statistics = null;
                    break;
                }
                LocalDateTime startTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
                LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
                statistics = statisticService.getStatistics(shopId, startTime, endTime);
                break;
            default:
                httpStatus = HttpStatus.BAD_REQUEST;
                message = "Invalid period";
                statistics = null;
                break;

        }
        return ResponseEntity.status(httpStatus).body(
                createResponse(
                        httpStatus,
                        message,
                        statistics
                )
        );
    }


    @GetMapping("/{shopId}/top-user-bought")
    public ResponseEntity<?> getTopUserBought(@PathVariable Long shopId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(orderService.getAllUserBoughtOfThisShop(shopId, page, size));
    }


}
