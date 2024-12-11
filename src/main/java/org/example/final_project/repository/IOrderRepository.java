package org.example.final_project.repository;

import org.example.final_project.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("select o.id from OrderEntity o where o.orderCode = :orderCode")
    int findIdByOrderCode(String orderCode);

    @Query("select o.totalPrice  from OrderEntity o where o.orderCode = :orderCode")
    Double findAmountByOrderCode(String orderCode);
}
