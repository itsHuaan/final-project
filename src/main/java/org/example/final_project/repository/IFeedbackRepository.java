package org.example.final_project.repository;

import org.example.final_project.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IFeedbackRepository extends JpaRepository<FeedbackEntity,Long>, JpaSpecificationExecutor<FeedbackEntity> {
}
