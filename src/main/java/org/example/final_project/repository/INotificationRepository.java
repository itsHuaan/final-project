package org.example.final_project.repository;

import org.example.final_project.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface INotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n from NotificationEntity n where n.userId = :userId ")
    Page<NotificationEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select n from NotificationEntity n where n.userId = :userId order by n.createdAt desc ")
    List<NotificationEntity> findListByUserId(@Param("userId") Long userId);

    @Query("select n from NotificationEntity n where n.shopId = :shopId")
    Page<NotificationEntity> findByShopId(@Param("shopId") Long shopId, Pageable pageable);


    @Query("select n from NotificationEntity n where n.shopId = :shopId order by n.createdAt desc")
    List<NotificationEntity> findListByShopId(@Param("shopId") Long shopId);


}
