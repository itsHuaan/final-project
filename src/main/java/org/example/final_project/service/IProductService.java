package org.example.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService extends IBaseService<ProductDto, ProductModel,Long> {
    int saveCustom(ProductModel productModel) throws JsonProcessingException;
    int inActivateProduct(long id,int type, String note);
    Page<ProductDto> findAllByPage(Pageable pageable);
    Page<ProductDto> findAllByNameAndPage(String name,Pageable pageable);
    Page<ProductDto> getAllProductByStatus(int status,Pageable pageable);
    Page<ProductDto> getAllProductRelative(long productId,Pageable pageable);
    Page<ProductDto> getOtherProductOfShop(long productId,Pageable pageable);

    Page<ProductDto> getAllProductOfShop(long userId, Pageable pageable);
    Page<ProductDto> getAllProductByCategory(long categoryId,Pageable pageable);
    Page<ProductDto> getAllProductByPrice(double startPrice,double endPrice,Pageable pageable);

}
