package org.example.final_project.repository;

import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICartItemRepository extends JpaRepository<CartItemEntity, Long>, JpaSpecificationExecutor<CartItemEntity> {
    @Query("select c from CartItemEntity c where c.cart.cartId = :cartId")
    List<CartItemEntity> findByCartId(Long cartId);

    @Transactional
    @Modifying
    @Query("delete from CartItemEntity c where c.cartDetailId = :cartItemId")
    void deleteByCartId(long cartItemId);



}
