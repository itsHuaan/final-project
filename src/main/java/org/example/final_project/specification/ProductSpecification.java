package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class ProductSpecification {
    public static Specification<ProductEntity> isNotStatus(int status) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("isActive"), status);
    }

    public static Specification<ProductEntity> isStatus(int status) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), status);
    }

    public static Specification<ProductEntity> hasPromotion(Long promotionId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.join("promotions").get("id"), promotionId);
    }

    public static Specification<ProductEntity> isValid() {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.isNull(root.get("user").get("deletedAt")),
                        criteriaBuilder.equal(root.get("user").get("shop_status"), 1),
                        criteriaBuilder.isNull(root.get("deletedAt")),
                        criteriaBuilder.isNull(root.get("categoryEntity").get("deletedAt"))
                );
    }


    public static Specification<ProductEntity> isNotValid() {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.isNotNull(root.get("user").get("deletedAt")),
                        criteriaBuilder.notEqual(root.get("user").get("shop_status"), 1),
                        criteriaBuilder.isNotNull(root.get("deletedAt")),
                        criteriaBuilder.isNotNull(root.get("categoryEntity").get("deletedAt"))
                );
    }

    public static Specification<ProductEntity> hasName(String name) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<ProductEntity> hasCategoryId(long categoryId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoryEntity").get("id"), categoryId);
    }

    public static Specification<ProductEntity> notHaveId(long id) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("id"), id);
    }

    public static Specification<ProductEntity> hasUserId(long userId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ProductEntity> hasUserNotDeleted(long userId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("user").get("deletedAt"));
    }

    public static Specification<ProductEntity> hasCategory(List<Long> categoryId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                root.get("categoryEntity").get("id").in(categoryId);
    }

    public static Specification<ProductEntity> hasShopAddress(List<Long> addressId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                root.get("user").get("address_id_shop").in(addressId);
    }

    public static Specification<ProductEntity> hasPriceBetween(Double startPrice, Double endPrice) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.join("skuEntities").get("price"), startPrice, endPrice);
    }

    public static Specification<ProductEntity> hasAverageRatingGreaterThan(double ratingThreshold) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<FeedbackEntity> feedback = subquery.from(FeedbackEntity.class);
            subquery.select(criteriaBuilder.avg(feedback.get("rate")))
                    .where(criteriaBuilder.equal(feedback.get("product"), root));

            return criteriaBuilder.greaterThanOrEqualTo(subquery, ratingThreshold);
        };
    }

    public static Specification<ProductEntity> hasId(long productId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), productId);
    }

    public static Specification<ProductEntity> isNotDeleted() {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<ProductEntity> isBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }
}
