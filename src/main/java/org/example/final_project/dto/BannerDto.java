package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BannerDto {
    private long id;

    private Double price;

    private String image;

    private LocalDateTime createStart;

    private LocalDateTime createEnd;

    private LocalDateTime timeCreate;

    private int isActive;
    private long shopId;
}
