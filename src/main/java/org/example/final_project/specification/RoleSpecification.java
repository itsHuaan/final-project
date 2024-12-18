package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {
    public static Specification<RoleEntity> isRole(String roleName) {
        return (Root<RoleEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("roleName"), roleName);
    }
}
