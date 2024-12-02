package org.example.final_project.repository;

import org.example.final_project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @Query("select p from UserEntity p where p.shop_status IN (2) order by p.time_created_shop ASC ")
    List<UserEntity> findAllStatusWaited();
    @Query("SELECT p FROM UserEntity p WHERE p.shop_status IN (2) ORDER BY p.time_created_shop ASC")
    Page<UserEntity> findAllStatusWaitedPage(Pageable pageable);
    @Query("SELECT p FROM UserEntity p WHERE p.shop_status = :status ORDER BY p.time_created_shop ASC")
    Page<UserEntity> findAllShopPageByStatus( int status , Pageable pageable);
    @Query("select p from UserEntity p where p.shop_status = :status order by p.time_created_shop ASC ")
    List<UserEntity> findAllShopByStatus(int status);
    @Query("SELECT p FROM UserEntity p ORDER BY p.time_created_shop ASC")
    Page<UserEntity> getAllShopStatusPage( Pageable pageable);
    @Query("SELECT p FROM UserEntity p ORDER BY p.time_created_shop ASC")
    List<UserEntity> getAllShopStatus();



}
