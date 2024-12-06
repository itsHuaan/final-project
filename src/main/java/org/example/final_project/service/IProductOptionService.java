package org.example.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.model.ProductOptionsModel;

import java.util.List;

public interface IProductOptionService extends IBaseService<ProductOptionDto,ProductOptionsModel,Long> {
    List<ProductOptionDto> saveAllOption(List<String> jsonOptions) throws JsonProcessingException;
    ProductOptionsEntity saveCustom(ProductOptionsModel model);
}
