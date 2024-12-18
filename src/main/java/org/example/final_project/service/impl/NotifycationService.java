package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.NotificationMapper;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.repository.INotificationRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.INotificationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotifycationService implements INotificationService {
    INotificationRepository notificationRepository;
    IUserRepository userRepository;
    MediaUploadService mediaUploadService;
    @Override
    public int sentNotification(NotificationModel notificationModel) throws IOException {
        NotificationEntity notificationEntity;
        if(notificationModel.getRecipientId() == 0){
            List<UserEntity> allUsers = userRepository.findAll();
            for (UserEntity user : allUsers) {
                notificationModel.setRecipientId(user.getUserId());
                notificationEntity = NotificationMapper.toEntity(notificationModel);
                notificationEntity.setImage(notificationModel.getImage());
                notificationRepository.save(notificationEntity);
            }
        }else {
            notificationEntity = NotificationMapper.toEntity(notificationModel);
            notificationRepository.save(notificationEntity);
        }
        return 1;
    }



}
