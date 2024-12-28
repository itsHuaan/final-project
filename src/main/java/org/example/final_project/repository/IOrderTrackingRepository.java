package org.example.final_project.repository;

import org.example.final_project.entity.OrderTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderTrackingRepository extends JpaRepository<OrderTrackingEntity, Long>, JpaSpecificationExecutor<OrderTrackingEntity> {
    @Query("select t from OrderTrackingEntity  t where t.order.id = :orderId and t.shopId = :shopId")
    Optional<OrderTrackingEntity> findByOrderIdAndShopId(long orderId, long shopId);

    @Query("select t from OrderTrackingEntity  t where t.order.id = :orderId")
    List<OrderTrackingEntity> listOrderTracking(long orderId);

    @Query("select t.order.id from OrderTrackingEntity t where t.shopId = :shopId and t.status = :status")
    List<Long> findOrderIdsByShopIdAndStatus(long shopId, int status);

    @Query("select ot.status from OrderTrackingEntity ot where ot.shopId = :shopId and ot.order.id = :orderId")
    Integer findOrderIdByShopIdAndOrderId(long shopId, long orderId);

    @Query("select case when count(t) > 0 then 1 else 0 end from OrderTrackingEntity t where t.order.id = :orderId and t.paidDate is not null")
    int checkPaidDateExistByOrderId(@Param("orderId") long orderId);


    @Modifying
    @Query("UPDATE OrderTrackingEntity o SET o.status = :status WHERE o.order.id = :orderId")
    void updateStatusByOrderId(@Param("status") int status, @Param("orderId") Long orderId);


}
