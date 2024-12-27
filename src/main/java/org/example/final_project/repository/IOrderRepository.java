package org.example.final_project.repository;

import org.example.final_project.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("select o.id from OrderEntity o where o.orderCode = :orderCode")
    int findIdByOrderCode(String orderCode);

    @Query("select o.totalPrice  from OrderEntity o where o.orderCode = :orderCode and o.statusCheckout in (2) ")
    Double findAmountByOrderCode(String orderCode);

    @Query("SELECT o FROM OrderEntity o WHERE o.id IN :ids  ")
    Page<OrderEntity> findAllByIds(@Param("ids") List<Long> ids, Pageable pageable);


    @Query("SELECT o FROM OrderEntity o WHERE o.id IN :ids")
    List<OrderEntity> findAllSortById(List<Long> ids, Sort sort);

    @Query("SELECT o FROM OrderEntity o JOIN OrderTrackingEntity ot ON ot.order.id = o.id WHERE ot.shopId = :shopId AND o.orderCode = :orderCode")
    Optional<OrderEntity> findOrderIdByShopIdAndOrderCode(long shopId, String orderCode);

    @Query("select t.id from OrderEntity  t where t.user.userId = :userId")
    List<Long> findOrderIdsByUserId(long userId);

    @Query("select o.id from OrderEntity o where o.user.userId = :userId and o.orderCode = :orderCode")
    long findOrderIdByUserIdAndOrderCode(long userId, String orderCode);

    @Query("select o from OrderEntity o where o.id in :id")
    List<OrderEntity> findByIds(List<Long> id);

    boolean existsByOrderCode(String orderCode);


}
