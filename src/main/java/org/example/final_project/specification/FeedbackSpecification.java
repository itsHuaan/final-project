package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.FeedbackEntity;
import org.springframework.data.jpa.domain.Specification;

public class FeedbackSpecification {
    public static Specification<FeedbackEntity> hasProductId(long productId) {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    public static Specification<FeedbackEntity> hasComment(boolean hasComment) {
        return hasComment
                ? (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("content"))
                : (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("content"));
    }

    public static Specification<FeedbackEntity> hasImage(boolean hasImage) {
        return hasImage
                ? (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.isNotNull(root.join("feedbackImages", JoinType.LEFT).get("id"))
                : (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.isNull(root.join("feedbackImages", JoinType.LEFT).get("id"));
    }


    public static Specification<FeedbackEntity> hasRatingGreaterThanOrEqualTo(double rating) {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), rating);
    }

    public static Specification<FeedbackEntity> hasRating(double rating) {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("rate"), rating);
    }
}
