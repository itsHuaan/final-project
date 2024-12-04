package org.example.final_project.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CategoryMapper {

    public CategoryDto convertToDto(CategoryEntity categoryEntity){
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .parent_id(categoryEntity.getParent_id())
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
}
