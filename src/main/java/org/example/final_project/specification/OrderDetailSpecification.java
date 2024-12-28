package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.OrderDetailEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderDetailSpecification {
    public static Specification<OrderDetailEntity> hasShop(long shopId) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("shopId"), shopId);
    }

    public static Specification<OrderDetailEntity> isValid() {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.isNull(root.get("skuEntity").get("product").get("user").get("deletedAt")),
                        criteriaBuilder.equal(root.get("skuEntity").get("product").get("user").get("shop_status"), 1),
                        criteriaBuilder.equal(root.get("skuEntity").get("product").get("isActive"), 1),
                        criteriaBuilder.isNull(root.get("skuEntity").get("product").get("deletedAt")),
                        criteriaBuilder.isNull(root.get("skuEntity").get("product").get("categoryEntity").get("deletedAt"))
                );
    }

    public static Specification<OrderDetailEntity> isBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.get("createAt"), startDate, endDate);
    }

    public static Specification<OrderDetailEntity> distinctUsersByShopIdAndDateRange(Long shopId, LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            assert criteriaQuery != null;
            criteriaQuery.distinct(true);
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("shopId"), shopId),
                    criteriaBuilder.between(root.join("orderEntity").get("createdAt"), startDate, endDate)
            );
        };
    }

    public static Specification<OrderDetailEntity> filterByShopAndDateRange(Long shopId, LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("shopId"), shopId),
                criteriaBuilder.between(root.join("orderEntity").get("createdAt"), startDate, endDate)
        );
    }

    public static Specification<OrderDetailEntity> soldSkuQuantityByShopIdAndDateRange(Long shopId, LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("shopId"), shopId),
                criteriaBuilder.between(root.join("orderEntity").get("createdAt"), startDate, endDate)
        );
    }

    public static Specification<OrderDetailEntity> hasStatus(long status) {
        return (Root<OrderDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.join("orderEntity").get("statusCheckout"), status);
    }
}
