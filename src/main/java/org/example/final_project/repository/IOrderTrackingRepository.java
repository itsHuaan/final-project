package org.example.final_project.repository;

import org.example.final_project.entity.OrderTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderTrackingRepository extends JpaRepository<OrderTrackingEntity, Long> {
    @Query("select t from OrderTrackingEntity  t where t.order.id = :orderId and t.shopId = :shopId")
    Optional<OrderTrackingEntity> findByOrderIdAndShopId(long orderId, long shopId);

//    @Query("select t from OrderTrackingEntity  t where t.order.id = :orderId")
//    long findOrderDetailsByOrderTrackingStatusZeroAndOrderId(long orderId);

    @Query("select t.order.id from OrderTrackingEntity t where t.shopId = :shopId and t.status = :status")
    List<Long> findOrderIdsByShopIdAndStatus(long shopId, int status);

    @Query("select ot.status from OrderTrackingEntity ot where ot.shopId = :shopId and ot.order.id = :orderId")
    int findOrderIdByShopIdAndOrderId(long shopId, long orderId);


}
