package org.example.final_project.mapper;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.Category;
import org.example.final_project.model.CategoryModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {
    public CategoryDto convertToDto(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .parent_id(category.getParent_id())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getCreatedAt())
                .deletedAt(category.getDeletedAt())
                .build();
    }
    public Category convertToEntity(CategoryModel model){
        return Category.builder()
                .name(model.getName())
                .parent_id(model.getParent_id())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
