package org.example.final_project.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.final_project.dto.FeedbackImageDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.model.FeedbackImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeedbackImageMapper {
    @Autowired
    Cloudinary cloudinary;

    public FeedbackImageDto convertToDto(FeedbackImageEntity image) {
        return FeedbackImageDto.builder()
                .id(image.getId())
                .imageLink(image.getImageLink())
                .build();
    }

    public FeedbackImageEntity convertToEntity(FeedbackImageModel feedbackImageModel) throws IOException {
        return FeedbackImageEntity.builder()
                .imageLink(feedbackImageModel != null
                        ? cloudinary.uploader().upload(feedbackImageModel.getFile().getBytes(), ObjectUtils.emptyMap()).get("url").toString()
                        : null)
                .build();
    }
}
