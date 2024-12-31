package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.service.INotificationService;
import org.example.final_project.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Notification")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/notification")
public class NotificationController {

    INotificationService notificationService;


    @PostMapping
    public ResponseEntity<?> sentNotification(@RequestBody List<NotificationModel> notificationModel) throws IOException {
        int result = notificationService.sentNotification(notificationModel);
        return ResponseEntity.ok(result == 1 ? "Thông báo đã được  gửi " : "Thông báo chưa được gửi ");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotification(@PathVariable long userId, @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(notificationService.getAllNotificationsByUserId(userId, page, size));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> changeStatusNotificationForUser(@PathVariable long userId) {
        int result = notificationService.changeStatusNotificationForUser(userId);
        return ResponseEntity.ok(result == 1 ? "Đã đọc" : "Chưa đọc");
    }

    @PutMapping("/{shopId}/shop")
    public ResponseEntity<?> changeStatusNotificationForShop(@PathVariable long shopId) {
        int result = notificationService.changeStatusNotificationForShop(shopId);
        return ResponseEntity.ok(result == 1 ? "Đã đọc" : "Chưa đọc");
    }

    @GetMapping("/{shopId}/shop")
    public ResponseEntity<?> getNotificationByShopId(@PathVariable long shopId, @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(notificationService.getAllNotificationsByShopId(shopId, page, size));
    }


}
