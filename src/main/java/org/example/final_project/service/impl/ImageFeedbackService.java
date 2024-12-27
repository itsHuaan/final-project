package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.FeedbackImageDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.mapper.FeedbackImageMapper;
import org.example.final_project.model.FeedbackImageModel;
import org.example.final_project.repository.IImageFeedBackRepository;
import org.example.final_project.service.IImageFeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ImageFeedbackService implements IImageFeedbackService {
    IImageFeedBackRepository iImageFeedBackRepository;
    FeedbackImageMapper imageMapper;

    @Override
    public List<FeedbackImageDto> getAll() {
        return null;
    }

    @Override
    public FeedbackImageDto getById(Long id) {
        return null;
    }

    @Override
    public int save(FeedbackImageModel feedbackImageModel) {
        try {
            FeedbackImageEntity image = imageMapper.convertToEntity(feedbackImageModel);
            image.setFeedback(FeedbackEntity.builder().id(feedbackImageModel.getFeedbackId()).build());
            iImageFeedBackRepository.save(image);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Long aLong, FeedbackImageModel feedbackImageModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<FeedbackImageDto> findAllByFeedback(long id) {
        return iImageFeedBackRepository.findByFeedback_Id(id).stream().map(imageMapper::convertToDto).collect(Collectors.toList());
    }
}
