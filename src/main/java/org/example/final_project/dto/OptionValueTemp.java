package org.example.final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionValueTemp {
    private ProductOptionDto option;
    private ProductOptionValueDto value;
}
