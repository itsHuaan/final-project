package org.example.final_project.repository;

import org.example.final_project.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPromotionRepository extends JpaRepository<PromotionEntity,Long>, JpaSpecificationExecutor<PromotionEntity> {
}
