package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.dto.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductModel {
    private MultipartFile[] files;
    private String name;
    private long numberOfFeedBack;
    private long numberOfLike;
    private double rating;
    private String description;
    private long parent_id;
    private long quantity;
    private double price;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private long categoryId;

}
