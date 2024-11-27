package org.example.final_project.mapper;

import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IImageFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FeedbackMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    IImageFeedbackService iImageFeedbackService;
    public FeedbackDto convertToDto(FeedbackEntity feedback){
        return FeedbackDto.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .rate(feedback.getRate())
                .user(userMapper.toDto(feedback.getUser()))
                .feedbackImages(iImageFeedbackService.findAllByFeedback(feedback.getId()))
                .build();
    }
    public FeedbackEntity convertToEntity(FeedbackModel model){
        return FeedbackEntity.builder()
                .content(model.getContent())
                .rate(model.getRate())
                .product(iProductRepository.findById(model.getProductId()).get())
                .user(iUserRepository.findById(model.getUserId()).get())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
