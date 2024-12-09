package org.example.final_project.repository;

import org.example.final_project.entity.ProductOptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductOptionRepository extends JpaRepository<ProductOptionsEntity,Long> {

}
