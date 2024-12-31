package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.FeedbackDto;
import org.example.final_project.model.FeedbackModel;
import org.example.final_project.service.IFeedbackService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(Const.API_PREFIX + "/feedback")
public class FeedbackController {
    IFeedbackService feedbackService;

    @Operation(summary = "Leave a feedback about the product")
    @PreAuthorize("isAuthenticated()")
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(
                            HttpStatus.BAD_REQUEST,
                            e.getMessage(),
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

    @Operation(summary = "Filter the feedback")
    @GetMapping("/{product-id}")
    ResponseEntity<?> filter(@PathVariable("product-id") long productId,
                             @RequestParam(required = false) Boolean hasImage,
                             @RequestParam(required = false) Boolean hasComment,
                             @RequestParam(required = false) Double rating) {
        List<FeedbackDto> result = feedbackService.filterFeedback(productId, hasImage, hasComment, rating);
        return !result.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Feedbacks fetched",
                        result
                )
        )
                : ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.NO_CONTENT,
                        "No feedback found",
                        result
                )
        );
    }
}
