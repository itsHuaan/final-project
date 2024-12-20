package org.example.final_project.repository;

import org.example.final_project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("SELECT p FROM UserEntity p WHERE p.email = :email")
    Optional<UserEntity> findByEmail(String email);

    @Query("select p from  UserEntity p where  p.shop_name like %:shopName% ")
    List<UserEntity> findByShopName(String shopName);

    @Query("select p from  UserEntity p where   p.shop_status= :shopStatus")
    List<UserEntity> findByShopStatus(Integer shopStatus);

    @Query("select p from UserEntity p where p.shop_status = :shopStatus and p.shop_name like %:shopName%")
    List<UserEntity> findByShopStatusAndName(Integer shopStatus, String shopName);

    @Query("select COUNT(u) > 0 from UserEntity u where u.shop_name = :shopName")
    boolean existsByShopName(@Param("shopName") String shopName);

    @Query("select u from UserEntity u where u.userId in :userId ")
    List<UserEntity> findByUserId(List<Long> userId);

    @Query("select u from UserEntity u where u.userId in :userId")
    Page<UserEntity> findByUserId(List<Long> userId, Pageable pageable);


}
