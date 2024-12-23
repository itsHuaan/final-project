package org.example.final_project.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.FeedbackImageDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.model.FeedbackImageModel;
import org.springframework.beans.factory.annotation.Autowired;
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
                        ? mediaUploadService.uploadMediaFile(feedbackImageModel.getFile())
                        : null)
                .build();
    }
}
