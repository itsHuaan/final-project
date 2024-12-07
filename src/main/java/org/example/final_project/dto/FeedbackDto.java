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
    private UserFeedBackDto user;
    private double rate;
    private String content;
//    private ProductDto product;
//    private List<FeedbackImageDto> feedbackImages;
}
