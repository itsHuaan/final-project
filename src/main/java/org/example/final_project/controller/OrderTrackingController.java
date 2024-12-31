package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.StatusMessageDto;
import org.example.final_project.model.CancelOrderModel;
import org.example.final_project.service.IOrderDetailService;
import org.example.final_project.service.IOrderTrackingService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "Order Tracking")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/user-tracking")
public class OrderTrackingController {
    IOrderDetailService orderDetailService;

    IOrderTrackingService orderTrackingService;

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> index(@PathVariable Long userId) {
        return ResponseEntity.ok(orderDetailService.getOrderDetail(userId));
    }

    @GetMapping("/{userId}/ship-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findByStatusShipping(@PathVariable Long userId,
                                                  @RequestParam Long shippingStatus) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailByShippingStatus(userId, shippingStatus));
    }

    @GetMapping("/{userId}/detail-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> detailOrderUser(@PathVariable long userId, @RequestParam long orderId, @RequestParam long shopId) {
        try {
            return ResponseEntity.ok(orderDetailService.findOrderDetailInfo(userId, orderId, shopId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Order Item");
        }
    }

    @GetMapping("/{userId}/find-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findOrder(@PathVariable long userId, @RequestParam String orderCode) {
        try {
            return ResponseEntity.ok(orderDetailService.findOrderInfoByOrderCode(userId, orderCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Order");
        }
    }

    @PostMapping("/change-status-ship")
    public ResponseEntity<?> statusShip(@RequestBody StatusMessageDto statusMessageDto) {
        int result = orderTrackingService.updateStatusShipping(statusMessageDto);
        return ResponseEntity.ok(result == 1 ? " Change Status Complete " : "Complete Deleted");
    }

    @PutMapping("/{orderDetailId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateFeedbackStatus(@PathVariable Long orderDetailId) {
        int result = orderDetailService.updateFeedbackStatus(orderDetailId);
        HttpStatus status = result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        String message = result == 1 ? "Successfully" : "Failed";
        return ResponseEntity.status(status).body(createResponse(
                status,
                message,
                null
        ));
    }

    @PutMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancel(@RequestBody CancelOrderModel cancelOrderModel) {
        return ResponseEntity.ok(orderDetailService.cancelOrder(cancelOrderModel));
    }
}
