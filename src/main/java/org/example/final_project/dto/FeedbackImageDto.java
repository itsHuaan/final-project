package org.example.final_project.dto;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedbackImageDto {
    private long id;
    private String imageLink;
}
