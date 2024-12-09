package org.example.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.model.SKUModel;

import java.io.IOException;
import java.util.List;

public interface ISKUService extends IBaseService<SKUDto, SKUModel,Long> {
    List<SKUDto> getAllByProduct(long productId);
    SKUDto saveCustom(SKUModel model) throws IOException;
    List<SKUDto> addListSKU(long productId, List<ProductOptionDto> optionList) throws IOException;
    int updateListStock(List<SKUModel> skuModels) throws IOException;
}
