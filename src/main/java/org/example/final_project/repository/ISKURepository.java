package org.example.final_project.repository;

import org.example.final_project.entity.SKUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISKURepository extends JpaRepository<SKUEntity, Long>, JpaSpecificationExecutor<SKUEntity> {
    List<SKUEntity> findAllByProduct_Id(long productId);

    List<SKUEntity> findAllByOption1_IdOrOption2_Id(long optionId1, long optionId2);

    @Query("select s.product.id from SKUEntity s where s.id = :SkuId")
    long findProductIdbySKUId(long SKUId);

    @Query("select s from SKUEntity s where s.id = :SkuId")
    Optional<SKUEntity> findbySKUId(long SKUId);


}
