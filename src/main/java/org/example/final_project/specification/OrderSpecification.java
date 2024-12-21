package org.example.final_project.specification;

import jakarta.persistence.criteria.*;
import org.example.final_project.entity.OrderEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static Specification<OrderEntity> hasShopIdAndCreatedAtRange(long shopId, LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.join("user").get("shopId"), shopId),
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate)
        );
    }
}
