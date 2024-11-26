package org.example.final_project.mapper;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.model.CategoryModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {
    public CategoryDto convertToDto(CategoryEntity categoryEntity){
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .parent_id(categoryEntity.getParent_id())
                .createdAt(categoryEntity.getCreatedAt())
                .modifiedAt(categoryEntity.getCreatedAt())
                .deletedAt(categoryEntity.getDeletedAt())
                .build();
    }
    public CategoryEntity convertToEntity(CategoryModel model){
        return CategoryEntity.builder()
                .name(model.getName())
                .parent_id(model.getParent_id())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
