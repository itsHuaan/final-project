package org.example.final_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductOptionDetailDto {
    private long id;
    private String name;
    private List<ProductOptionValueDto> values;
}
