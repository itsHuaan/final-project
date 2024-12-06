package org.example.final_project.repository;

import org.example.final_project.entity.OtpEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IOtpRepository extends JpaRepository<OtpEntity, Long>, JpaSpecificationExecutor<OtpEntity> {
}
