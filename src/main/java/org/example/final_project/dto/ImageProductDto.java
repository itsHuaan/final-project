package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageProductDto {
    private long id;
    private String imageLink;
    private ProductDto productDto;
}
