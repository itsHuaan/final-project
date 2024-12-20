package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ShopStatisticDto;
import org.example.final_project.service.IOrderService;
import org.example.final_project.service.IStatisticService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getShopDetail(@PathVariable Long shopId, @RequestParam Long orderId) {
        return ResponseEntity.ok(orderService.getOrderTracking(orderId, shopId));
    }

    @GetMapping("/{shopId}/order")
    public ResponseEntity<?> getOrder(@PathVariable long shopId, @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer statusShip) {
        return ResponseEntity.ok(orderService.getOrdersByShopId(shopId, page, size, statusShip));
    }

    @GetMapping("/{shopId}/find-order")
    public ResponseEntity<?> findOrder(@PathVariable Long shopId, @RequestParam String orderCode) {
        try {
            return ResponseEntity.ok(orderService.findByShopIdAndCodeOrder(shopId, orderCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Order");
        }
    }

    @GetMapping("/{shopId}/periodic-statistics")
    public ResponseEntity<?> getPeriodicStatistics(@PathVariable Long shopId) {
        List<ShopStatisticDto> periodicStatistics = statisticService.getPeriodicStatistics(shopId);
        return periodicStatistics != null
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Fetched",
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

    @GetMapping("/{shopId}/statistics")
    public ResponseEntity<?> getStatistic(@PathVariable Long shopId,
                                          @RequestParam String period,
                                          @RequestParam(required = false) LocalDate startDate,
                                          @RequestParam(required = false) LocalDate endDate) {
        ShopStatisticDto statistics = new ShopStatisticDto();
        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Fetched";
        switch (period.toLowerCase()) {
            case "today":
                statistics = statisticService.getStatistics(shopId, START_OF_DAY, END_OF_DAY);
                break;
            case "this_week":
                statistics = statisticService.getStatistics(shopId, START_OF_WEEK, END_OF_DAY);
                break;
            case "this_month":
                statistics = statisticService.getStatistics(shopId, START_OF_MONTH, END_OF_DAY);
                break;
            case "this_year":
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
