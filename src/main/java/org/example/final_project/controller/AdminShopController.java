package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.service.IOrderService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ADMIN SHOP")
@RestController
@RequestMapping(Const.API_PREFIX + "/shop")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminShopController {
    IOrderService orderService;


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

//    @GetMapping("/{shopId}/find-status-shipping")
//    public ResponseEntity<?> findStatusShipping(@PathVariable Long shopId, @RequestParam int statusShipping) {
//        return ResponseEntity.ok(orderService.findByStatusShipping(shopId, statusShipping));
//    }
    
}
