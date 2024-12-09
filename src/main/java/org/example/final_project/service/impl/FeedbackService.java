package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.mapper.FeedbackMapper;
import org.example.final_project.model.FeedbackImageModel;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IFeedbackRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IFeedbackService;
import org.example.final_project.service.IImageFeedbackService;
import org.example.final_project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService implements IFeedbackService {
    IFeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;
    IImageFeedbackService iImageFeedbackService;
    IUserRepository userRepository;
    IProductRepository productRepository;

    @Override
    public List<FeedbackDto> getAll() {
        return null;
    }

    @Override
    public FeedbackDto getById(Long id) {
        if (feedbackRepository.findById(id).isPresent()) {
            return feedbackMapper.convertToDto(feedbackRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public int save(FeedbackModel feedbackModel) {
        FeedbackEntity feedback = feedbackRepository.save(feedbackMapper.convertToEntity(feedbackModel));
        for (MultipartFile image : feedbackModel.getFiles()){
            iImageFeedbackService.save(new FeedbackImageModel(image, feedback.getId()));
        }
        return 1;
    }

    @Override
    public int update(Long aLong, FeedbackModel feedbackModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
