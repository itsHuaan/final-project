package org.example.final_project.repository;

import org.example.final_project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @Query("select p from UserEntity p where p.shop_status IN (1, 2, 3) order by p.time_created_shop ASC ")
    List<UserEntity> findAllStatusUserBeingShop();
    @Query("SELECT p FROM UserEntity p WHERE p.shop_status IN (1, 2, 3) ORDER BY p.time_created_shop ASC")
    Page<UserEntity> findAllStatusUserBeingShopPage(Pageable pageable);
    @Query("SELECT p FROM UserEntity p WHERE p.shop_status IN (1, 4) ORDER BY p.time_created_shop ASC")
    Page<UserEntity> findAllShopActivePage(Pageable pageable);
    @Query("select p from UserEntity p where p.shop_status IN (1, 4) order by p.time_created_shop ASC ")
    List<UserEntity> findAllShopActive();


}
