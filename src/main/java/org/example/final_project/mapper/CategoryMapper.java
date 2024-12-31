package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.CategoryDto;
import org.example.final_project.dto.CategorySummaryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.CategoryModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryMapper {
    MediaUploadService mediaUploadService;

    public CategoryDto convertToDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .categoryId(categoryEntity.getId())
                .categoryName(categoryEntity.getName())
                .parentId(categoryEntity.getParent_id())
                .image(categoryEntity.getImage())
                .createdAt(categoryEntity.getCreatedAt())
                .modifiedAt(categoryEntity.getCreatedAt())
                .deletedAt(categoryEntity.getDeletedAt())
                .build();
    }

    public CategoryEntity convertToEntity(CategoryModel model) throws IOException {
        return CategoryEntity.builder()
                .name(model.getName())
                .parent_id(model.getParent_id())
                .image(model.getFile() != null
                        ? mediaUploadService.uploadSingleMediaFile(model.getFile())
                        : null)
                .user(model.getUser_id() != null
                        ? UserEntity.builder()
                        .userId(model.getUser_id())
                        .build()
                        : null)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CategorySummaryDto toCategorySummaryDto(CategoryEntity categoryEntity) {
        return CategorySummaryDto.builder()
                .categoryId(categoryEntity.getId())
                .categoryName(categoryEntity.getName())
                .image(categoryEntity.getImage())
                .build();
    }
}
