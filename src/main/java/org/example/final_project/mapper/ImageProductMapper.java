package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.entity.ImageProductEntity;
import org.example.final_project.model.ImageProductModel;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageProductMapper {
    MediaUploadService mediaUploadService;

    public ImageProductDto convertToDto(ImageProductEntity image) {
        return ImageProductDto.builder()
                .id(image.getId())
                .imageLink(image.getImageLink())
                .build();
    }

    public ImageProductEntity convertToEntity(ImageProductModel model) throws IOException {
        return ImageProductEntity.builder()
                .imageLink(mediaUploadService.uploadSingleMediaFile(model.getMultipartFile()))
                .build();
    }
}
