package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.IOrderDetailService;
import org.example.final_project.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order Tracking")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/user-tracking")
public class OrderTrackingController {
        IOrderDetailService orderDetailService;
        @GetMapping("/{userId}")
        public ResponseEntity<?> index(@PathVariable Long userId) {
            return ResponseEntity.ok(orderDetailService.getOrderDetail(userId));
        }

        @GetMapping("")
        public ResponseEntity<?> index(@RequestParam Long userId , @RequestParam Long shippingStatus) {
            return ResponseEntity.ok(orderDetailService.getOrderDetailFlowShippingStatus(userId, shippingStatus));
        }







}
