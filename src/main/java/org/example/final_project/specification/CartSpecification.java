package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.springframework.data.jpa.domain.Specification;

public class CartSpecification {
    public static Specification<CartEntity> hasUserId(long userId) {
        return (Root<CartEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("userId"), userId);
    }
}
