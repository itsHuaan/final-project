package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/option")
@Tag(name = "Product Option")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ROLE_SELLER')")
public class ProductOptionController {
    IProductOptionService optionService;
    ISKUService iskuService;

    @PostMapping("/{product-id}")
    ResponseEntity<?> addNewOption(@PathVariable("product-id") Long productId,
                                   @RequestBody ProductOptionsModel model) {
        try {
            if (optionService.getNumberOfOptionByProduct(productId) < 2) {
                Long savedOptionId = optionService.saveCustom(model).getId();
                iskuService.addListSKU(productId, savedOptionId);
                return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
                        HttpStatus.CREATED,
                        "Add new option successfully",
                        null
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Product can't have more than 2 options",
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

    @DeleteMapping("/{option-id}")
    ResponseEntity<?> deleteOption(@PathVariable("option-id") Long optionId,
                                   @RequestParam("productId") Long productId) {
        try {
            if (optionService.getNumberOfOptionByProduct(productId) > 1) {
                iskuService.addListSKUAfterDeleteOption(productId, optionId);
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.NO_CONTENT,
                        "Deleted option successfully",
                        null
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Product must have at least 1 option",
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

    @PutMapping("/{option-id}")
    ResponseEntity<?> updateOption(@PathVariable("option-id") Long optionId,
                                   @RequestBody ProductOptionsModel model) {
        try {
            optionService.update(optionId, model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Updated option successfully",
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
