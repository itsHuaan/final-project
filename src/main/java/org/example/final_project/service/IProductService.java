package org.example.final_project.service;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService extends IBaseService<ProductDto, ProductModel,Long> {
    int inActivateProduct(long id);
    List<ProductDto> findAllByPage(Pageable pageable);
}
