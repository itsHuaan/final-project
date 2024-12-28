package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_banner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BannerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private String image;

    private LocalDateTime createStart;

    private LocalDateTime createEnd;

    private LocalDateTime timeCreate;

    private int isActive;

    private long shopId;
}
