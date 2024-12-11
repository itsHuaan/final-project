package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query("select t.orderEntity.id from OrderDetailEntity t where t.shopId = :shopId")
    List<Long> findOrderIdsByShopId(long shopId);
}
