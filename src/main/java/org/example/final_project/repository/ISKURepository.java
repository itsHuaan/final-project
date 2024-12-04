package org.example.final_project.repository;

import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISKURepository extends JpaRepository<SKUEntity,Long> {
    List<SKUEntity> findAllByProduct_Id(long productId);
}
