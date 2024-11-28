package org.example.final_project.util.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.OtpEntity;
import org.example.final_project.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<ProductEntity> isStatus(int status) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), status);
    }
    public static Specification<ProductEntity> isNotDeleted() {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deletedAt"));
    }
    public static Specification<ProductEntity> hasName(String name) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), name);
    }
    public static Specification<ProductEntity> hasCategoryId(long categoryId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoryEntity").get("id"), categoryId);
    }
    public static Specification<ProductEntity> hasParentId(long parentId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), parentId);
    }
    public static Specification<ProductEntity> notHaveId(long id) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("id"), id);
    }
    public static Specification<ProductEntity> hasUserId(long userId) {
        return (Root<ProductEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
