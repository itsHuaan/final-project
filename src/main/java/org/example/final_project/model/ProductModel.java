package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.controller.ProductOptionRequest;
import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductModel {
    private MultipartFile[] files;
    private String name;
    private String description;
    private String note;
    private long categoryId;
    private long user_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String options;
}
