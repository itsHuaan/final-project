package org.example.final_project.service;

import org.example.final_project.dto.FeedbackImageDto;
import org.example.final_project.model.FeedbackImageModel;

import java.util.List;

public interface IImageFeedbackService extends IBaseService<FeedbackImageDto, FeedbackImageModel,Long> {
    List<FeedbackImageDto> findAllByFeedback(long id);
}
