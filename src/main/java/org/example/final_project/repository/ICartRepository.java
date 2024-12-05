package org.example.final_project.repository;

import org.example.final_project.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ICartRepository extends JpaRepository<CartEntity, Long>, JpaSpecificationExecutor<CartEntity> {

}
