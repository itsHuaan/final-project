package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.mapper.FeedbackMapper;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IFeedbackRepository;
import org.example.final_project.service.IFeedbackService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.final_project.specification.FeedbackSpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService implements IFeedbackService {
    IFeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;
    MediaUploadService mediaUploadService;

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
        FeedbackEntity feedback = feedbackMapper.convertToEntity(feedbackModel);
        feedbackRepository.save(feedback);
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

    @Override
    public List<FeedbackDto> filterFeedback(long productId, Boolean hasImage, Boolean hasComment, Double rating) {
        Specification<FeedbackEntity> spec = Specification.where(hasProductId(productId));
        if (hasImage != null) {
            spec = spec.and(hasImage(hasImage));
        }
        if (hasComment != null) {
            spec = spec.and(hasComment(hasComment));
        }
        if (rating != null) {
            spec = spec.and(hasRating(rating));
        }
        return feedbackRepository.findAll(spec, Sort.by(Sort.Order.desc("createdAt"))).stream()
                .map(feedbackMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
