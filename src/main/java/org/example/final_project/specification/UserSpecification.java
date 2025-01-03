package org.example.final_project.specification;

import jakarta.persistence.criteria.*;
import org.example.final_project.entity.*;
import org.example.final_project.enumeration.ShopStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecification {
    public static Specification<UserEntity> isInactive() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), 0);
    }

    public static Specification<UserEntity> isActive() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), 1);
    }

    public static Specification<UserEntity> hasUsername(String username) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("username"), username);
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<UserEntity> isNotSuperAdmin() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("role").get("roleName"), "ROLE_ADMIN");
    }

    public static Specification<UserEntity> isNotDeleted() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("deletedAt"));
    }


    public static Specification<UserEntity> isDeleted() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("deletedAt"));
    }

    public static Specification<UserEntity> hasId(Long id) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), id);
    }

    public static Specification<UserEntity> doesNotHaveShopStatus(int status) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("shop_status"), status);
    }

    public static Specification<UserEntity> hasShopStatus(int status) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("shop_status"), status);
    }

    public static Specification<UserEntity> isShop() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("shop_status"), ShopStatus.ACTIVE.getValue());
    }

    public static Specification<UserEntity> containName(String name) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<UserEntity> hasStatus(int status) {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), status);
    }

    public static Specification<UserEntity> hasNewlyJoined() {
        return (Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }
}
