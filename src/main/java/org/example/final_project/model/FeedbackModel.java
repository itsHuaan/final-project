package org.example.final_project.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackModel {
    private long userId;
    private long productId;
    private double rate;
    private String content;
    private MultipartFile[] files;
}
