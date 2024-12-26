package org.example.final_project.service;

import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.model.ProductOptionValueModel;

import java.io.IOException;

public interface IProductOptionValueService extends IBaseService<ProductOptionValueDto, ProductOptionValueModel, Long> {
    int saveCustom(Long productId, ProductOptionValueModel valueModel) throws IOException;
}
