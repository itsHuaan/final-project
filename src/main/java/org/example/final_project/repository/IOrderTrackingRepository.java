package org.example.final_project.repository;

import org.example.final_project.entity.OrderTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderTrackingRepository extends JpaRepository<OrderTrackingEntity,Long> {
    @Query("select t from OrderTrackingEntity  t where t.order.id = :orderId and t.shopId = :shopId")
    Optional<OrderTrackingEntity> findByOrderIdAndShopId( long orderId, long shopId );


}
