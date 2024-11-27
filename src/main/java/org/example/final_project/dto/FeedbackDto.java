package org.example.final_project.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.UserEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackDto {
    private long id;
    private String content;
    private UserDto user;
    private double rate;
    private ProductDto product;
    private List<FeedbackImageDto> feedbackImages;
}
