package org.example.final_project.repository;

import org.example.final_project.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {
    @Query("select p from ProductEntity p where p.parent_id=:parentId")
    List<ProductEntity> findAllByParent_id(@Param("parentId") long parentId);
}
