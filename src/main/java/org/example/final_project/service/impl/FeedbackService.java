package org.example.final_project.service.impl;

import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.mapper.FeedbackMapper;
import org.example.final_project.model.FeedbackImageModel;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.repository.IFeedbackRepository;
import org.example.final_project.service.IFeedbackService;
import org.example.final_project.service.IImageFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    IFeedbackRepository feedbackRepository;
    @Autowired
    FeedbackMapper feedbackMapper;
    @Autowired
    IImageFeedbackService iImageFeedbackService;
    @Override
    public List<FeedbackDto> getAll() {
        return null;
    }

    @Override
    public FeedbackDto getById(Long id) {
        if (feedbackRepository.findById(id).isPresent()) {
            return feedbackMapper.convertToDto(feedbackRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public int save(FeedbackModel feedbackModel) {
        try{
            FeedbackEntity feedback=feedbackRepository.save(feedbackMapper.convertToEntity(feedbackModel));
            for(MultipartFile file:feedbackModel.getFiles()){
                iImageFeedbackService.save(new FeedbackImageModel(file, feedback.getId()));
            }
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int update(Long aLong, FeedbackModel feedbackModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
