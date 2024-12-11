package org.example.final_project.util.specification;

import jakarta.persistence.criteria.*;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.FeedbackImageEntity;
import org.example.final_project.entity.OtpEntity;
import org.springframework.data.jpa.domain.Specification;

public class FeedbackSpecification {
    public static Specification<FeedbackEntity> hasProductId(long productId) {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    public static Specification<FeedbackEntity> hasComment() {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("content"));
    }

    public static Specification<FeedbackEntity> hasImage() {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Join<FeedbackEntity, FeedbackImageEntity> imagesJoin = root.join("feedbackImages", JoinType.LEFT);

            return criteriaBuilder.isNotNull(imagesJoin.get("id"));
        };
    }


    public static Specification<FeedbackEntity> hasRatingGreaterThanOrEqualTo(double rating) {
        return (Root<FeedbackEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rate"), rating);
    }
}
