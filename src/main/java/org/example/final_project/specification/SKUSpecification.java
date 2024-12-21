package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.SKUEntity;
import org.springframework.data.jpa.domain.Specification;

public class SKUSpecification {
    public static Specification<SKUEntity> hasShop(long shopId) {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("user").get("userId"), shopId);
    }

    public static Specification<SKUEntity> isLowStock() {
        return (Root<SKUEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), 100),
                        criteriaBuilder.greaterThan(root.get("quantity"), 0)
                );
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
