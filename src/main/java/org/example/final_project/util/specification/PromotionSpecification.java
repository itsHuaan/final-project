package org.example.final_project.util.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.PromotionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PromotionSpecification {
    public static Specification<PromotionEntity> isNotDeleted() {
        return (Root<PromotionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<PromotionEntity> isActiveButNotActivatedAndHaveProduct(Long productId) {
        return (Root<PromotionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.or(
                                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), LocalDateTime.now()),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), LocalDateTime.now())
                        ),
                        criteriaBuilder.equal(root.join("products").get("id"), productId)
                );
    }
}
