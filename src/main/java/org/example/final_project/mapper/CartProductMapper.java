package org.example.final_project.mapper;

import org.example.final_project.dto.CartProductDto;
import org.example.final_project.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class CartProductMapper {
    public CartProductDto toDto(ProductEntity productEntity){
        return CartProductDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .productImage(productEntity.getImages().get(0).getImageLink())
                .categoryId(productEntity.getCategoryEntity().getId())
                .categoryName(productEntity.getCategoryEntity().getName())
                .shopId(productEntity.getUser().getUserId())
                .shopName(productEntity.getUser().getShop_name())
                .build();
    }
}
