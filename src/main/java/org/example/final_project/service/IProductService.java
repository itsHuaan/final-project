package org.example.final_project.service;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService extends IBaseService<ProductDto, ProductModel,Long> {
    int inActivateProduct(long id,int type, String note);
    Page<ProductDto> findAllByPage(Pageable pageable);
    Page<ProductDto> findAllByNameAndPage(String name,Pageable pageable);
    Page<ProductDto> getAllByParentId(long parentId,Pageable pageable);
    Page<ProductDto> getAllProductByStatus(int status,Pageable pageable);
    Page<ProductDto> getAllProductRelative(long productId,Pageable pageable);
    Page<ProductDto> getOtherProductOfShop(long productId,Pageable pageable);

    Page<ProductDto> getAllProductOfShop(long userId, Pageable pageable);
}
