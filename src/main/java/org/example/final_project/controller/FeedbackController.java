package org.example.final_project.controller;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.service.IFeedbackService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(Const.API_PREFIX+"/feedback")
public class FeedbackController {
    @Autowired
    IFeedbackService feedbackService;
    @PostMapping("/addNew")
    ResponseEntity<ApiResponse<?>> addNewFeedback(@ModelAttribute FeedbackModel model){
        if(feedbackService.save(model)==1){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    204,
                    "Feedback Successfully",
                    null,
                    LocalDateTime.now()
            ));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Bad Request",
                    null,
                    LocalDateTime.now()
                    )
            );
        }
    }
}
