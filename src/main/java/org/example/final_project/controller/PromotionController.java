package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.model.PromotionModel;
import org.example.final_project.service.IPromotionService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;
import static org.example.final_project.model.validation.PageableValidation.setDefault;

@RestController
@Tag(name = "Promotion")
@RequestMapping(Const.API_PREFIX + "/promotion")
public class PromotionController {
    @Autowired
    IPromotionService promotionService;

    @GetMapping
    ResponseEntity<?> getAllPromotion(@RequestParam(required = false) Integer pageIndex,
                                      @RequestParam(required = false) Integer pageSize) {
        if (setDefault(pageSize, pageIndex) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    promotionService.findAllByPage(setDefault(pageSize, pageIndex))
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    "Invalid index or size",
                    null
            ));
        }
    }

    @PostMapping
    ResponseEntity<?> addPromotion(@RequestBody PromotionModel model) {
        try {
            if (LocalDateTime.now().isBefore(model.getStartDate())) {
                promotionService.save(model);
                return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
                        HttpStatus.CREATED,
                        "Successfully",
                        null
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Promotion must be later or equal to today",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{promotion-id}")
    ResponseEntity<?> updatePromotion(@PathVariable("promotion-id") Long promotionId, @RequestBody PromotionModel model) {
        try {
            promotionService.update(promotionId, model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{promotion-id}")
    ResponseEntity<?> deletePromotion(@PathVariable("promotion-id") Long promotionId) {
        try {
            promotionService.delete(promotionId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping("/apply-promotion")
    ResponseEntity<?> applyPromotion(@RequestParam Long promotionId,
                                     @RequestBody List<Long> productIds) {
        try {
            promotionService.applyPromotion(promotionId, productIds);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.CREATED,
                    "Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/cancel")
    ResponseEntity cancelPromotionOfProduct(@RequestParam Long promotionId,
                                            @RequestParam Long productId) {
        try {
            promotionService.cancelPromotionOfProduct(promotionId, productId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }


}
