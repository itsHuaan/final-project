package org.example.final_project.repository;

import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductOptionValueRepository extends JpaRepository<ProductOptionValuesEntity,Long> {
    List<ProductOptionValuesEntity> findAllByOption_Id(long id);

    @Query("select p.name from  ProductOptionValuesEntity p where  p.id = :id")
    String findById(long id);
}
