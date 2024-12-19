package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    @Query("select t.orderEntity.id from OrderDetailEntity t where t.shopId = :shopId")
    List<Long> findOrderIdsByShopId(long shopId);

    @Query("select t from OrderDetailEntity t where t.shopId = :shopId and t.orderEntity.id = :orderId")
    List<OrderDetailEntity> shopOrder(long shopId, long orderId);

    @Query("select o from OrderDetailEntity o where o.orderEntity.id in :orderIds order by o.createAt desc")
    List<OrderDetailEntity> findAllOrderDetailEntityByOrderId(List<Long> orderIds);

    @Query("select od from OrderDetailEntity od join od.orderEntity oe join oe.orderTrackingEntities ot where ot.status = :status and oe.id in :orderIds ")
    List<OrderDetailEntity> findOrderDetailsByOrderTrackingStatusZeroAndOrderId(long status, List<Long> orderIds);

    @Query("select od.order.id from OrderTrackingEntity od where od.status = :status")
    List<Long> findOrderDetailsByStatus(long status);


    @Query("select od from OrderDetailEntity od " +
            "JOIN OrderEntity o " +
            "on o.id = od.orderEntity.id " +
            "WHERE od.id = :orderDetailId AND o.user.userId = :userId")
    Optional<OrderDetailEntity> findOrderDetailByOrderDetailIdAndUserId(
            @Param("orderDetailId") long orderDetailId,
            @Param("userId") long userId);

    @Query("select o from OrderDetailEntity o where o.orderEntity.id = :orderIds")
    List<OrderDetailEntity> findByOrderId(long orderIds);


//    @Query("select distinct o.user.userId from OrderEntity o join OrderDetailEntity od on o.id == od.orderEntity.id where od.shopId = :shopId")
//    List<Long> findAllCustomerBoughtAtThísShop(long shopId);

}