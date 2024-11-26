package org.example.final_project.repository;

import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.entity.ImageProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageProductRepository extends JpaRepository<ImageProductEntity,Long> {
    List<ImageProductEntity> findAllByProductEntity_Id(long id);
}
