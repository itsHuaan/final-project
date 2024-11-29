package org.example.final_project.repository;

import org.example.final_project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    @Query("select p from UserEntity p where p.shop_status = 1 or p.shop_status = 2 or p.shop_status =3 order by p.time_created_shop ASC ")
    List<UserEntity> findAllStatusUserBeingShop();
}
