package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategorySummaryDto {
    private long categoryId;
    private String categoryName;
    private String image;
}
