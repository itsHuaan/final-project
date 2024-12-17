package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.OtpEntity;
import org.example.final_project.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OtpSpecification {
    public static Specification<OtpEntity> isOtp(final String otp) {
        return (Root<OtpEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("otpCode"), otp);
    }

    public static Specification<OtpEntity> isActive() {
        return (Root<OtpEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), 1);
    }

    public static Specification<OtpEntity> createdWithinLastMinutes(int minutes) {
        return (Root<OtpEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime threshold = now.minusMinutes(minutes);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), threshold);
        };
    }

    public static Specification<OtpEntity> hasEmail(String email) {
        return (Root<OtpEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("email"), email);
    }
}
