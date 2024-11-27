package org.example.final_project.util.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.CategoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<CategoryEntity> isActive() {
        return (Root<CategoryEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), 1);
    }
    public static Specification<CategoryEntity> isNotDeleted() {
        return (Root<CategoryEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deletedAt"), null);
    }
}
