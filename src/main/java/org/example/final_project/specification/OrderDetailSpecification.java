package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderDetailSpecification {
    public static Specification<OrderDetailEntity> hasShop(long shopId) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("shopId"), shopId);
    }
}
