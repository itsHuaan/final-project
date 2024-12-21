package org.example.final_project.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.final_project.entity.ChatRoomEntity;
import org.springframework.data.jpa.domain.Specification;

public class ChatRoomSpecification {
    public static Specification<ChatRoomEntity> hasSenderId(long senderId) {
        return (Root<ChatRoomEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("senderId"), senderId);
    }

    public static Specification<ChatRoomEntity> hasRecipientId(long recipientId) {
        return (Root<ChatRoomEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("recipientId"), recipientId);
    }

    public static Specification<ChatRoomEntity> hasChatId(String chatId) {
        return (Root<ChatRoomEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("chatId"), chatId);
    }

    public static Specification<ChatRoomEntity> hasNormalizedChat(Long firstId, Long secondId) {
        return (Root<ChatRoomEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("senderId"), firstId),
                criteriaBuilder.equal(root.get("recipientId"), secondId)
        );
    }
}
