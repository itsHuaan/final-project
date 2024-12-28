package org.example.final_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductSummaryDto;
import org.example.final_project.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService extends IBaseService<ProductDto, ProductModel, Long> {
    int saveCustom(ProductModel productModel) throws JsonProcessingException;

    int deactivateProduct(long id, int type, String note);

    Page<ProductSummaryDto> findAllByPage(Integer type, Pageable pageable);

    Page<ProductSummaryDto> findAllByNameAndPage(Integer type, String name, Pageable pageable);

    Page<ProductSummaryDto> getAllProductByStatus(int status, Pageable pageable);

    Page<ProductSummaryDto> getAllProductRelative(long productId, Pageable pageable);

    Page<ProductSummaryDto> getOtherProductOfShop(long productId, Pageable pageable);

    Page<ProductSummaryDto> getAllProductOfShop(long userId, Integer type, Pageable pageable);


    ProductDto getByIdCustom(Long productId, Integer type);

    Page<ProductSummaryDto> getAllProductByFilter(Integer type, String name, List<Long> categoryId, List<Long> addressId, Double startPrice, Double endPrice, Double rating, Pageable pageable);

    Page<ProductSummaryDto> getAllProductByPromotion(Integer type, Long promotionId, Long shopId, Pageable pageable);
}
