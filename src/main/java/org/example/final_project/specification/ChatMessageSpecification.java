package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.ChatMessageEntity;
import org.springframework.data.jpa.domain.Specification;

public class ChatMessageSpecification {
    public static Specification<ChatMessageEntity> hasSenderId(Long senderId) {
        return (Root<ChatMessageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                senderId != null ? criteriaBuilder.equal(root.get("senderId"), senderId) : null;
    }

    public static Specification<ChatMessageEntity> hasRecipientId(Long recipientId) {
        return (Root<ChatMessageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                recipientId != null ? criteriaBuilder.equal(root.get("recipientId"), recipientId) : null;
    }

    public static Specification<ChatMessageEntity> hasChatId(String chatId) {
        return (Root<ChatMessageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("chatId"), chatId);
    }

    public static Specification<ChatMessageEntity> hasSeen(int status) {
        return (Root<ChatMessageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isSeen"), status);
    }
}
