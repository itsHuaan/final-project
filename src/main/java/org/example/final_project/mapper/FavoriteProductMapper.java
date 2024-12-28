package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.entity.FavoriteProductEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.FavoriteProductModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteProductMapper {
    IProductRepository productRepository;
    IUserRepository userRepository;
    public FavoriteProductEntity toFavoriteProductEntity(FavoriteProductModel favoriteProductModel) {
        UserEntity user = userRepository.findById(favoriteProductModel.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        ProductEntity product = productRepository.findById(favoriteProductModel.getProductId()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        return FavoriteProductEntity.builder()
                .user(user)
                .product(product)
                .build();
    }
}
