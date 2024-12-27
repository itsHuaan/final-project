package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.PromotionModel;
import org.example.final_project.service.IPromotionService;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;
import static org.example.final_project.validation.PageableValidation.setDefault;

@RestController
@Tag(name = "Promotion")
@RequestMapping(Const.API_PREFIX + "/promotion")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionController {
    IPromotionService promotionService;

    @GetMapping
    ResponseEntity<?> getAllPromotion(@RequestParam(required = false) Integer pageIndex,
                                      @RequestParam(required = false) Integer pageSize) {
        if (setDefault(pageSize, pageIndex) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all promotion successfully",
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<?> addPromotion(@RequestBody PromotionModel model) {
        try {
            if (LocalDateTime.now().isBefore(model.getStartDate())) {
                promotionService.save(model);
                return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
                        HttpStatus.CREATED,
                        "Added promotion successfully",
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{promotion-id}")
    ResponseEntity<?> updatePromotion(@PathVariable("promotion-id") Long promotionId, @RequestBody PromotionModel model) {
        try {
            promotionService.update(promotionId, model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Updated promotion successfully",
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{promotion-id}")
    ResponseEntity<?> deletePromotion(@PathVariable("promotion-id") Long promotionId) {
        try {
            promotionService.delete(promotionId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Deleted promotion successfully",
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

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PostMapping("/apply-promotion")
    ResponseEntity<?> applyPromotion(@RequestParam Long promotionId,
                                     @RequestBody List<Long> productIds) {
        try {
            promotionService.applyPromotion(promotionId, productIds);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.CREATED,
                    "Apply promotion successfully",
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

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @DeleteMapping("/cancel")
    ResponseEntity<?> cancelPromotionOfProduct(@RequestParam Long promotionId,
                                               @RequestBody List<Long> productIds) {
        try {
            promotionService.cancelPromotionOfProduct(promotionId, productIds);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Canceled promotion successfully",
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

    @GetMapping("/{shop-id}")
    ResponseEntity<?> getAllPromotionByShop(@PathVariable("shop-id") Long shopId,
                                            @RequestParam(required = false) Integer pageIndex,
                                            @RequestParam(required = false) Integer pageSize) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all promotion successfully",
                    promotionService.getAllByShop(shopId, PageableValidation.setDefault(pageSize, pageIndex))
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
