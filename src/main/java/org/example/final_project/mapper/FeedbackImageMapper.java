package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.FeedbackImageDto;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.model.FeedbackImageModel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackImageMapper {
    MediaUploadService mediaUploadService;

    public FeedbackImageDto convertToDto(FeedbackImageEntity image) {
        return FeedbackImageDto.builder()
                .id(image.getId())
                .imageLink(image.getImageLink())
                .build();
    }

    public FeedbackImageEntity convertToEntity(FeedbackImageModel feedbackImageModel) throws IOException {
        return FeedbackImageEntity.builder()
                .imageLink(feedbackImageModel != null
                        ? mediaUploadService.uploadSingleMediaFile(feedbackImageModel.getFile())
                        : null)
                .build();
    }
}
