package org.example.final_project.repository;

import org.example.final_project.entity.FavoriteProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IFavoriteProductRepository extends JpaRepository<FavoriteProductEntity, Long>, JpaSpecificationExecutor<FavoriteProductEntity> {
    Optional<FavoriteProductEntity> findByProductIdAndUserUserId(Long productId, Long userId);
}
