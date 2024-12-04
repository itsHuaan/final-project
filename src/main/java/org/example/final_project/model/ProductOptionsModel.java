package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.final_project.dto.ProductDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductOptionsModel {
    private String name;
    private long productId;
    private List<ProductOptionsValueModel> optionValues;
}
