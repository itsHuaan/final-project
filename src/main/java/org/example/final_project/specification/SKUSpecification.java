package org.example.final_project.specification;

import jakarta.persistence.criteria.*;
import org.example.final_project.entity.*;
import org.springframework.data.jpa.domain.Specification;

public class SKUSpecification {
    public static Specification<SKUEntity> hasShop(long shopId) {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("user").get("userId"), shopId);
    }

    public static Specification<SKUEntity> isLowStock(int quantity) {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), quantity),
                        criteriaBuilder.greaterThan(root.get("quantity"), 0)
                );
    }

    public static Specification<SKUEntity> isValid() {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Join<SKUEntity, ProductEntity> product = root.join("product", JoinType.LEFT);
            Join<ProductEntity, CategoryEntity> category = product.join("categoryEntity", JoinType.LEFT);

            Predicate productNotDeleted = criteriaBuilder.isNull(product.get("deletedAt"));
            Predicate categoryNotDeleted = criteriaBuilder.isNull(category.get("deletedAt"));

            return criteriaBuilder.and(productNotDeleted, categoryNotDeleted);
        };
    }


    public static Specification<SKUEntity> hasRatingAbove(double rating) {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
        {
            assert criteriaQuery != null;
            Subquery<Double> avgRatingSubquery = criteriaQuery.subquery(Double.class);
            Root<FeedbackEntity> feedbackRoot = avgRatingSubquery.from(FeedbackEntity.class);
            avgRatingSubquery.select(criteriaBuilder.avg(feedbackRoot.get("rate")))
                    .where(criteriaBuilder.equal(feedbackRoot.get("product").get("id"), root.get("product").get("id")));
            return criteriaBuilder.greaterThan(avgRatingSubquery, rating);
        };
    }
}
