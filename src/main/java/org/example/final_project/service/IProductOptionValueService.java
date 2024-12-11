package org.example.final_project.service;

import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.model.SKUModel;

import java.io.IOException;
import java.util.List;

public interface IProductOptionValueService extends IBaseService<ProductOptionValueDto, ProductOptionValueModel,Long> {
    int saveCustom(Long productId,ProductOptionValueModel valueModel) throws IOException;
}
