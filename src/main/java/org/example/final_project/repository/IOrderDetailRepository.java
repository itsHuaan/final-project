package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
}
