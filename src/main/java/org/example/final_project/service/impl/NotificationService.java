package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.NotificationDto;
import org.example.final_project.entity.NotificationEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.enumeration.NotificationStatus;
import org.example.final_project.mapper.NotificationMapper;
import org.example.final_project.model.NotificationModel;
import org.example.final_project.repository.INotificationRepository;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.INotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService implements INotificationService {
    INotificationRepository notificationRepository;
    IUserRepository userRepository;
    IOrderDetailRepository orderDetailRepository;


    @Override
    public int sentNotification(List<NotificationModel> notificationModels) {
        for (NotificationModel notificationModel : notificationModels) {
            if (notificationModel.getAdminId() == 1) {
                if (notificationModel.getUserId() == 0) {
                    List<UserEntity> allUsers = userRepository.findAll();
                    for (UserEntity user : allUsers) {
                        NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                        notificationEntity.setUserId(user.getUserId());
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
                if (notificationModel.getUserId() == 0) {
                    List<Long> userId = orderDetailRepository.findAllCustomerBoughtAtThisShop(notificationModel.getAdminId());
                    for (Long id : userId) {
                        NotificationEntity notificationEntity = NotificationMapper.toEntity(notificationModel);
                        notificationEntity.setUserId(id);
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
    public ApiResponse<?> getAllNotificationsByUserId(long userId, Integer page, Integer size) {
        if (page == null || size == null) {
            List<NotificationEntity> notificationEntityList = notificationRepository.findListByUserId(userId);
            List<NotificationDto> list1 = notificationEntityList.stream().map(NotificationMapper::toNotificationDto).toList();
            return createResponse(HttpStatus.OK, "Successfully Retrieved Users", list1);
        }
        if (page < 0 || size <= 0) {
            return createResponse(HttpStatus.OK, "Page must be >= 0 and size must be >= 1 ", null);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<NotificationEntity> notificationEntityList = notificationRepository.findByUserId(userId, pageable);
        Page<NotificationDto> notificationDtoPage = notificationEntityList.map(NotificationMapper::toNotificationDto);

        return ApiResponse.createResponse(HttpStatus.OK, "get all notifications", notificationDtoPage);
    }

    @Override
    public int changeStatusNotificationForUser(long userId) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findListByUserId(userId);
        for (NotificationEntity notificationEntity : notificationEntityList) {
            if (notificationEntity.getIsRead() == 0) {
                notificationEntity.setIsRead(NotificationStatus.READ.ordinal());
                notificationRepository.save(notificationEntity);
            }
        }
        return 1;
    }

    @Override
    public int changeStatusNotificationForShop(long shopId) {
        List<NotificationEntity> notificationEntityList = notificationRepository.findListByShopId(shopId);
        for (NotificationEntity notificationEntity : notificationEntityList) {
            if (notificationEntity.getIsRead() == 0) {
                notificationEntity.setIsRead(NotificationStatus.READ.ordinal());
                notificationRepository.save(notificationEntity);
            }
        }
        return 1;
    }


    @Override
    public ApiResponse<?> getAllNotificationsByShopId(long shopId, Integer page, Integer size) {
        if (page == null || size == null) {
            List<NotificationEntity> notificationEntityList = notificationRepository.findListByShopId(shopId);
            List<NotificationDto> list1 = notificationEntityList.stream().map(NotificationMapper::toNotificationDto).toList();
            return createResponse(HttpStatus.OK, "Successfully Retrieved Users", list1);
        }
        if (page < 0 || size <= 0) {
            return createResponse(HttpStatus.OK, "Page must be >= 0 and size must be >= 1 ", null);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<NotificationEntity> notificationEntityList = notificationRepository.findByShopId(shopId, pageable);
        Page<NotificationDto> notificationDtoPage = notificationEntityList.map(NotificationMapper::toNotificationDto);

        return ApiResponse.createResponse(HttpStatus.OK, "get all notifications", notificationDtoPage);

    }


}
