package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.NotificationModel;

import java.io.IOException;
import java.util.List;

public interface INotificationService {
    int sentNotification(List<NotificationModel> notificationModel) throws IOException;

    ApiResponse<?> getAllNotificationsByUserId(long userId, Integer page, Integer size);


    int changeStatusNotificationForUser(long userId);

    ApiResponse<?> getAllNotificationsByShopId(long shopId, Integer page, Integer size);


    int changeStatusNotificationForShop(long shopId);
}
