package org.example.final_project.repository;

import org.example.final_project.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageProductRepository extends JpaRepository<ImageProduct,Long> {
}
