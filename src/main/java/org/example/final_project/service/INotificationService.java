package org.example.final_project.service;

import org.example.final_project.model.NotificationModel;

import java.io.IOException;

public interface INotificationService {
    int sentNotification(NotificationModel notificationModel) throws IOException;

}
