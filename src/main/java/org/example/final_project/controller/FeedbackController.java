package org.example.final_project.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(Const.API_PREFIX + "/feedback")
public class FeedbackController {
    IFeedbackService feedbackService;

    @PostMapping
    ResponseEntity<?> addNewFeedback(FeedbackModel feedback) {
        try {
            feedbackService.save(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    createResponse(
                            HttpStatus.CREATED,
                            "Feedback added",
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(
                            HttpStatus.BAD_REQUEST,
                            "Failed to add feedback",
                            null
                    )
            );
        }
    }
}
