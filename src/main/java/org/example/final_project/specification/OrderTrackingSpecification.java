package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.OrderTrackingEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderTrackingSpecification {
    public static Specification<OrderTrackingEntity> hasShopIdAndCreatedAtRangeAndStatus(long shopId, LocalDateTime startDate, LocalDateTime endDate, int status) {
        return (Root<OrderTrackingEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("shopId"), shopId),
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate),
                criteriaBuilder.equal(root.get("status"), status)
        );
    }

    public static Specification<OrderTrackingEntity> hasShop(long shopId) {
        return (Root<OrderTrackingEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("shopId"), shopId);
    }

    public static Specification<OrderTrackingEntity> isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return (Root<OrderTrackingEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), startTime, endTime);
    }

    public static Specification<OrderTrackingEntity> hasStatus(int status) {
        return (Root<OrderTrackingEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }
}
