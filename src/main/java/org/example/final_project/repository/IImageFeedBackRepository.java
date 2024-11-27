package org.example.final_project.repository;

import org.example.final_project.entity.FeedbackImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface IImageFeedBackRepository extends JpaRepository<FeedbackImageEntity,Long> {
    List<FeedbackImageEntity> findByFeedback_Id(long id);
}
