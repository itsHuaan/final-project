package org.example.final_project.controller;

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

@RestController
@RequestMapping(Const.API_PREFIX+"/feedback")
public class FeedbackController {
    @Autowired
    IFeedbackService feedbackService;
    @PostMapping("/addNew")
    ResponseEntity addNewFeedback(@ModelAttribute FeedbackModel model){
        if(feedbackService.save(model)==1){
            return ResponseEntity.status(HttpStatus.OK).body("Feedback Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Occur Error");
        }
    }
}
