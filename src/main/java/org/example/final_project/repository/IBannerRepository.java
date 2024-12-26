package org.example.final_project.repository;

import org.example.final_project.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBannerRepository extends JpaRepository<BannerEntity, Long> {
}
