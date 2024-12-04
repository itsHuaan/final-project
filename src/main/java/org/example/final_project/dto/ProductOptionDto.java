package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductOptionDto {
    private long id;
    private String name;
    private List<ProductOptionValueDto> values;
}
