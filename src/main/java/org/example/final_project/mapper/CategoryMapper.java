package org.example.final_project.mapper;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.dto.CategorySummaryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.model.CategoryModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CategoryMapper {

    public CategoryDto convertToDto(CategoryEntity categoryEntity){
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
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
    public CategorySummaryDto toCategorySummaryDto(CategoryEntity categoryEntity){
        return CategorySummaryDto.builder().categoryId(categoryEntity.getId())
                .categoryName(categoryEntity.getName())
                .image(categoryEntity.getImage())
                .build();
    }
}
