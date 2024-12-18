package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.service.INotificationService;
import org.example.final_project.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Notification")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = Const.API_PREFIX + "/notification")
public class NotificationController {
    INotificationService notificationService;


    @PostMapping("/sent")
    public ResponseEntity<?> sentNotification(@RequestBody NotificationModel notificationModel) throws IOException {
        int result = notificationService.sentNotification(notificationModel);
        return ResponseEntity.ok(result == 1 ? "Thông báo đã được  gửi " : "Thông báo chưa được gửi ");
    }


}
