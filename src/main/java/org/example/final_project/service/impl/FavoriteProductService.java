package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.FavoriteProductDto;
import org.example.final_project.entity.FavoriteProductEntity;
import org.example.final_project.mapper.FavoriteProductMapper;
import org.example.final_project.model.FavoriteProductModel;
import org.example.final_project.repository.IFavoriteProductRepository;
import org.example.final_project.service.IFavoriteProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteProductService implements IFavoriteProductService {
    IFavoriteProductRepository favoriteProductRepository;
    FavoriteProductMapper favoriteProductMapper;

    @Override
    public List<FavoriteProductDto> getAll() {
        return List.of();
    }

    @Override
    public FavoriteProductDto getById(Long id) {
        return null;
    }

    @Override
    public int save(FavoriteProductModel favoriteProductModel) {
        Optional<FavoriteProductEntity> existingProduct = favoriteProductRepository.findByProductIdAndUserUserId(favoriteProductModel.getProductId(), favoriteProductModel.getUserId());
        if (existingProduct.isPresent()) {
            favoriteProductRepository.delete(existingProduct.get());
            return 0;
        } else {
            favoriteProductRepository.save(favoriteProductMapper.toFavoriteProductEntity(favoriteProductModel));
            return 1;
        }
    }

    @Override
    public int update(Long aLong, FavoriteProductModel favoriteProductModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
