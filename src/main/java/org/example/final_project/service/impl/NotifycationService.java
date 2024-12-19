package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.NotificationDto;
import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.NotificationMapper;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.model.enum_status.StatusNotification;
import org.example.final_project.repository.INotificationRepository;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.INotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotifycationService implements INotificationService {
    INotificationRepository notificationRepository;
    IUserRepository userRepository;
    IOrderDetailRepository orderDetailRepository;


    @Override
    public int sentNotification(List<NotificationModel> notificationModels) throws IOException {
        for (NotificationModel notificationModel : notificationModels) {
            if (notificationModel.getSenderId() == 1) {
                if (notificationModel.getRecipientId() == 0) {
                    List<UserEntity> allUsers = userRepository.findAll();
                    for (UserEntity user : allUsers) {
                        NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                        notificationEntity.setRecipientId(user.getUserId());
                        notificationEntity.setImage(notificationModel.getImage());
                        notificationEntity.setCreatedAt(LocalDateTime.now());
                        notificationRepository.save(notificationEntity);
                    }
                } else {
                    NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                    notificationEntity.setImage(notificationModel.getImage());
                    notificationEntity.setCreatedAt(LocalDateTime.now());
                    notificationRepository.save(notificationEntity);
                }
            } else {
                if (notificationModel.getRecipientId() == 0) {
                    List<Long> recipientId = orderDetailRepository.findAllCustomerBoughtAtThisShop(notificationModel.getSenderId());
                    for (Long id : recipientId) {
                        NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                        notificationEntity.setRecipientId(id);
                        notificationEntity.setImage(notificationModel.getImage());
                        notificationEntity.setCreatedAt(LocalDateTime.now());
                        notificationRepository.save(notificationEntity);
                    }
                } else {
                    NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                    notificationEntity.setImage(notificationModel.getImage());
                    notificationEntity.setCreatedAt(LocalDateTime.now());
                    notificationRepository.save(notificationEntity);
                }
            }
        }
        return 1;
    }


    @Override
    public ApiResponse<?> getAllNotificationsByUserId(long userId) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findByRecipientId(userId);
        List<NotificationDto> notificationDtos = notificationEntityList.stream().map(NotificationMapper::toNotificationDto).toList();
        return ApiResponse.createResponse(HttpStatus.OK, "get all notifications", notificationDtos);

    }

    @Override
    public int changeStatusNotification(long userId) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findByRecipientId(userId);
        for (NotificationEntity notificationEntity : notificationEntityList) {
            if (notificationEntity.getIsRead() == 0) {
                notificationEntity.setIsRead(StatusNotification.Read.ordinal());
                notificationRepository.save(notificationEntity);
            }
        }
        return 1;
    }


}
