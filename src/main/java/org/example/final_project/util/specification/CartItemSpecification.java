package org.example.final_project.util.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class CartItemSpecification {
    public static Specification<CartItemEntity> hasCartId(long cartId) {
        return (Root<CartItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cart").get("cartId"), cartId);
    }

    public static Specification<CartItemEntity> hasProductId(long productId) {
        return (Root<CartItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }
}
