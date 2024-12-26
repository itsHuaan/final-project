package org.example.final_project.specification;

import jakarta.persistence.criteria.*;
import org.example.final_project.entity.*;
import org.example.final_project.enumeration.ShopStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    public static Specification<UserEntity> isInactive() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), 0);
    }

    public static Specification<UserEntity> isActive() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), 1);
    }

    public static Specification<UserEntity> hasUsername(String username) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("username"), username);
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<UserEntity> isNotSuperAdmin() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("role").get("roleName"), "ROLE_ADMIN");
    }

    public static Specification<UserEntity> isNotDeleted() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deletedAt"));
    }


    public static Specification<UserEntity> isDeleted() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("deletedAt"));
    }

    public static Specification<UserEntity> hasId(Long id) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), id);
    }

    public static Specification<UserEntity> hasShopStatus(int status) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("shop_status"), status);
    }

    public static Specification<UserEntity> isShop() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("shop_status"), ShopStatus.ACTIVE.getValue());
    }

    public static Specification<UserEntity> containName(String name) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<UserEntity> hasStatus(int status) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), status);
    }

    public static Specification<UserEntity> hasNewlyJoined() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), LocalDateTime.now(), LocalDateTime.now().minusDays(7));
    }

    public static Specification<UserEntity> sortedBySoldProductRatingRatio() {
        return (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // Join tables
            Join<UserEntity, ProductEntity> products = root.join("products", JoinType.LEFT);
            Join<ProductEntity, SKUEntity> skus = products.join("skuEntities", JoinType.LEFT);
            Join<SKUEntity, OrderDetailEntity> orderDetails = skus.join("orderDetails", JoinType.LEFT);
            Join<ProductEntity, FeedbackEntity> feedbacks = products.join("feedbacks", JoinType.LEFT);

            // Calculate total quantity sold for the shop
            Expression<Long> totalQuantitySold = cb.sum(orderDetails.get("quantity"));

            // Calculate weighted rating sum (rating * quantity sold for each product)
            Expression<Double> weightedRatingSum = cb.sum(
                    cb.prod(feedbacks.get("rate"), orderDetails.get("quantity"))
            );

            // Weighted average rating for the shop
            Expression<Object> weightedAverageRating = cb.selectCase()
                    .when(cb.equal(totalQuantitySold, 0L), 0.0)
                    .otherwise(cb.quot(weightedRatingSum, totalQuantitySold));

            // Ensure shop is active and has products
            Subquery<Long> productExists = query.subquery(Long.class);
            Root<ProductEntity> productRoot = productExists.from(ProductEntity.class);
            productExists.select(cb.literal(1L))
                    .where(cb.equal(productRoot.get("user"), root));

            Predicate shopActive = cb.equal(root.get("shop_status"), 1);
            Predicate hasProducts = cb.exists(productExists);

            // Group by user and sort by weighted average rating
            query.groupBy(root.get("userId"));
            query.orderBy(cb.desc(weightedAverageRating));

            return cb.and(shopActive, hasProducts);
        };
    }

}
