package org.example.final_project.repository;

import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductOptionValueRepository extends JpaRepository<ProductOptionValuesEntity,Long> {
    List<ProductOptionValuesEntity> findAllByOption_Id(long id);
}
