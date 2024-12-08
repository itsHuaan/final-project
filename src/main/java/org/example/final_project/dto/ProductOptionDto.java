package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductOptionDto {
    private long optionId;
    private String name;
    private ProductOptionValueDto value;
}
