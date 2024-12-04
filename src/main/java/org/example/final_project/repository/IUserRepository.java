package org.example.final_project.repository;

import org.example.final_project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("SELECT p FROM UserEntity p WHERE p.email = :email")
    Optional<UserEntity> findByEmail(String email);


    Optional<UserEntity> findByUsername(String username);
}
