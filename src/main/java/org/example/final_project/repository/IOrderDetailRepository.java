package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.OrderEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query("select t.orderEntity.id from OrderDetailEntity t where t.shopId = :shopId")
    List<Long> findOrderIdsByShopId(long shopId);

    @Query("select t from OrderDetailEntity t where t.shopId = :shopId and t.orderEntity.id = :orderId")
    List<OrderDetailEntity> shopOrder(long shopId , long orderId);

    @Query("SELECT o FROM OrderDetailEntity o WHERE o.orderEntity.id IN :orderIds order by o.createAt desc")
    List<OrderDetailEntity> findAllOrderDetailEntityByOrderId(List<Long> orderIds);

    @Query("SELECT od FROM OrderDetailEntity od JOIN od.orderEntity oe JOIN oe.orderTrackingEntities ot WHERE ot.status = :status AND oe.id IN :orderIds ")
    List<OrderDetailEntity> findOrderDetailsByOrderTrackingStatusZeroAndOrderId(long status, List<Long> orderIds);


}
