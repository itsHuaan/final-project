package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerModel {

    private Double price;

    private MultipartFile image;

    private LocalDateTime createStart;

    private LocalDateTime createEnd;

    private long shopId;
}
