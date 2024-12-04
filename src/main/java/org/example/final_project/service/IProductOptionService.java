package org.example.final_project.service;

import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.model.ProductOptionsModel;

import java.util.List;

public interface IProductOptionService extends IBaseService<ProductOptionDto,ProductOptionsModel,Long> {
    List<ProductOptionDto> getAllByProduct(long productId);
}
