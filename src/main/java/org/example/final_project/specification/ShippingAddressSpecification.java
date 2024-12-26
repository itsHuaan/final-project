package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.UserShippingAddressEntity;
import org.springframework.data.jpa.domain.Specification;

public class ShippingAddressSpecification {
    public static Specification<UserShippingAddressEntity> ofUser(long userId) {
        return (Root<UserShippingAddressEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("userId"), userId);
    }
    public static Specification<UserShippingAddressEntity> hasAddress(long addressId) {
        return (Root<UserShippingAddressEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address").get("id"), addressId);
    }
}
