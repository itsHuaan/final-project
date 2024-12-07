package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IImageFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackMapper {
    UserMapper userMapper;
    IProductRepository iProductRepository;
    IUserRepository iUserRepository;
    IImageFeedbackService iImageFeedbackService;

    public FeedbackDto convertToDto(FeedbackEntity feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .user(userMapper.toUserFeedBackDto(feedback.getUser()))
                .content(feedback.getContent())
                .rate(feedback.getRate())
//                .feedbackImages(iImageFeedbackService.findAllByFeedback(feedback.getId()))
                .build();
    }

    public FeedbackEntity convertToEntity(FeedbackModel model) {
        ProductEntity product = iProductRepository.findById(model.getProductId()).isPresent()
                ? iProductRepository.findById(model.getProductId()).get()
                : null;
        UserEntity user = iUserRepository.findById(model.getUserId()).isPresent()
                ? iUserRepository.findById(model.getUserId()).get()
                : null;
        return FeedbackEntity.builder()
                .content(model.getContent())
                .rate(model.getRate())
                .product(product)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
