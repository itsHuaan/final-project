package org.example.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;

import java.util.List;

public interface IProductOptionService extends IBaseService<ProductOptionDetailDto, ProductOptionsModel, Long> {
    List<ProductOptionDetailDto> saveAllOption(String jsonOptions) throws JsonProcessingException;

    ProductOptionsEntity saveCustom(ProductOptionsModel model);

    int getNumberOfOptionByProduct(Long productId);

}
