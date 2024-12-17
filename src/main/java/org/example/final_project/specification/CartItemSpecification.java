package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CartItemSpecification {
    public static Specification<CartItemEntity> hasCartId(long cartId) {
        return (Root<CartItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cart").get("cartId"), cartId);
    }

    public static Specification<CartItemEntity> hasProductId(long productId) {
        return (Root<CartItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }

    public static Specification<CartItemEntity> hasProductIds(List<Long> productIds) {
        return (Root<CartItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                root.get("product").get("id").in(productIds);
    }
}
