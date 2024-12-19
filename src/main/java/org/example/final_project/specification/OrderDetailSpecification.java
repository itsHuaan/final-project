package org.example.final_project.specification;

import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrderDetailSpecification {
    public static Specification<OrderDetailEntity> hasShop(long shopId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shopId"), shopId);
    }
}
