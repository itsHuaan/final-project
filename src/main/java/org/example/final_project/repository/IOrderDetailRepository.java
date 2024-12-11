package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query("select t.orderEntity.id from OrderDetailEntity t where t.shopId = :shopId")
    List<Long> findOrderIdsByShopId(long shopId);

    @Query("select t from OrderDetailEntity t where t.shopId = :shopId and t.orderEntity.id = :orderId")
    List<OrderDetailEntity> shopOrder(long shopId , long orderId);
}
