package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.mapper.FeedbackMapper;
import org.example.final_project.model.FeedbackImageModel;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IFeedbackRepository;
import org.example.final_project.service.IFeedbackService;
import org.example.final_project.service.IImageFeedbackService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    IImageFeedbackService iImageFeedbackService;
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
        if (feedbackModel.getFiles() != null) {
            for (MultipartFile image : feedbackModel.getFiles()) {
                try {
                    FeedbackImageEntity feedbackImage = new FeedbackImageEntity();
                    feedbackImage.setImageLink(mediaUploadService.uploadSingleMediaFile(image));
                    feedbackImage.setFeedback(feedback);
                    feedback.getFeedbackImages().add(feedbackImage);
                } catch (IOException e) {
                    log.error("Error occurred while uploading image", e);
                    throw new RuntimeException("Image upload failed", e);
                }
            }
        }
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
    public List<FeedbackDto> filterFeedback(long productId, Integer hasImage, Integer hasComment, Double rating) {
        Specification<FeedbackEntity> spec = Specification.where(hasProductId(productId));
        if (hasImage != null && hasImage == 1) {
            spec = spec.and(hasImage());
        }
        if (hasComment != null && hasComment == 1) {
            spec = spec.and(hasComment());
        }
        if (rating != null) {
            spec = spec.and(hasRatingGreaterThanOrEqualTo(rating));
        }
        return feedbackRepository.findAll(spec, Sort.by(Sort.Order.desc("createdAt"))).stream()
                .map(feedbackMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
