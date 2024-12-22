package org.example.final_project.specification;

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

    public static Specification<PromotionEntity> isNotExpired() {
        return (Root<PromotionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), LocalDateTime.now());
    }


    public static Specification<PromotionEntity> isActiveForTheProduct(Long productId) {
        return (Root<PromotionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), LocalDateTime.now()),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), LocalDateTime.now())
                        ),
                        criteriaBuilder.equal(root.join("products").get("id"), productId)
                );
    }

    public static Specification<PromotionEntity> isActiveOrComingForTheShop(Long shopId) {
        return (Root<PromotionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.and(
                                criteriaBuilder.and(
                                        criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), LocalDateTime.now()),
                                        criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), LocalDateTime.now())
                                ),
                                criteriaBuilder.equal(root.join("products").get("user").get("userId"), shopId)
                        ),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), LocalDateTime.now())
                );
    }
}
