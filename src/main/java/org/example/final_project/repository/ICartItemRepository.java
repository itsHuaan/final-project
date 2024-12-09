package org.example.final_project.repository;

import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICartItemRepository extends JpaRepository<CartItemEntity, Long>, JpaSpecificationExecutor<CartItemEntity> {
    @Query("select c from CartItemEntity c where c.cart.cartId = :cartId")
    List<CartItemEntity> findByCartId(Long cartId);
}
