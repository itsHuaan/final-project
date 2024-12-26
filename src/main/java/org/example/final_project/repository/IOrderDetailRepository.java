package org.example.final_project.repository;

import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderDetailRepository extends JpaRepository<OrderDetailEntity, Long>, JpaSpecificationExecutor<OrderDetailEntity> {
    @Query("select t.orderEntity.id from OrderDetailEntity t where t.shopId = :shopId")
    List<Long> findOrderIdsByShopId(long shopId);

    @Query("select t from OrderDetailEntity t where t.shopId = :shopId and t.orderEntity.id = :orderId")
    List<OrderDetailEntity> shopOrder(long shopId, long orderId);

    @Query("select o from OrderDetailEntity o where o.orderEntity.id in :orderIds order by o.createAt desc")
    List<OrderDetailEntity> findAllOrderDetailEntityByOrderId(List<Long> orderIds);

    @Query("select od.order.id from OrderTrackingEntity od where od.status = :status")
    List<Long> findOrderDetailsByStatus(long status);


    @Query("select o from OrderDetailEntity o where o.orderEntity.id = :orderIds")
    List<OrderDetailEntity> findByOrderId(long orderIds);

    @Query("select distinct o.user.userId from OrderDetailEntity od join od.orderEntity o where od.shopId = :shopId")
    List<Long> findAllCustomerBoughtAtThisShop(@Param("shopId") long shopId);

    @Query("""
                select o.user.userId
                from OrderDetailEntity od
                join od.orderEntity o
                where od.shopId = :shopId
                and o.statusCheckout in (1, 2)
                group by o.user.userId
                order by count(o.user.userId) desc
            """)
    List<Long> findAllCustomerBoughtTheMostAtThisShop(@Param("shopId") long shopId);

    @Query("SELECT DISTINCT o.user FROM OrderDetailEntity od " +
            "JOIN od.orderEntity o " +
            "WHERE od.shopId = :shopId " +
            "AND o.createdAt BETWEEN :startDate AND :endDate")
    Page<UserEntity> findDistinctUsersByShopAndDateRange(@Param("shopId") Long shopId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate,
                                                         Pageable pageable);

    @Query("""
                SELECT DISTINCT od.skuEntity.product
                FROM OrderDetailEntity od 
                WHERE od.skuEntity.product.user.userId = :shopId
                  AND od.orderEntity.createdAt BETWEEN :startTime AND :endTime
                GROUP BY od.skuEntity.product
                ORDER BY SUM(od.quantity) DESC
            """)
    Page<ProductEntity> findTopPurchasedProductsByShop(@Param("shopId") Long shopId,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime,
                                                       Pageable pageable);

    @Query("select distinct o.orderEntity.id from OrderDetailEntity o where o.shopId = :shopId")
    List<Long> findAllOrderIdsByShopId(long shopId);


}

