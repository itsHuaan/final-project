package org.example.final_project.service;

import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.model.FeedbackModel;

import java.util.List;

public interface IFeedbackService extends IBaseService<FeedbackDto, FeedbackModel, Long> {
    List<FeedbackDto> filterFeedback(long productId, Boolean hasImage, Boolean hasComment, Double rating);
}
