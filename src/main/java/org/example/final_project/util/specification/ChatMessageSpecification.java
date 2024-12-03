package org.example.final_project.util.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.ChatMessageEntity;
import org.springframework.data.jpa.domain.Specification;

public class ChatMessageSpecification {
    public static Specification<ChatMessageEntity> hasChatId(String chatId) {
        return (Root<ChatMessageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("chatId"), chatId);
    }
}
