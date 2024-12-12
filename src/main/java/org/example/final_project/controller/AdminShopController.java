package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.final_project.service.IOrderService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ADMIN SHOP")
@RestController
@RequestMapping(Const.API_PREFIX + "/shop")
@RequiredArgsConstructor
public class AdminShopController {
    @Autowired
    private IOrderService orderService;
    @GetMapping("/detail-order")
    public ResponseEntity<?> getShopDetail(@RequestParam Long shopId , @RequestParam Long orderId ) {
        return ResponseEntity.ok(orderService.getOrderTracking(shopId, orderId));
    }

    @GetMapping("/{shopId}/order")
    public ResponseEntity<?> getOrder(@PathVariable long shopId , @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(orderService.getAllOrderByShopId(shopId , page, size));
    }

    @GetMapping("/find-order")
    public ResponseEntity<?> findOrder(@RequestParam Long shopId , @RequestParam String orderCode) throws Exception {
        try {
            return ResponseEntity.ok(orderService.findByShopIdAndCodeOrder(shopId,orderCode));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Order");
        }
    }
}
