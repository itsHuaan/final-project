package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/value")
@Tag(name = "Product Option Value")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ROLE_SELLER')")
public class ProductOptionValueController {
    IProductOptionValueService valueService;

    @PostMapping("/{product-id}")
    ResponseEntity<?> addNewValue(@PathVariable("product-id") Long productId,
                                  ProductOptionValueModel valueModel) {
        try {
            valueService.saveCustom(productId, valueModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
                    HttpStatus.CREATED,
                    "Add new value successfully",
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

    @DeleteMapping("/{value-id}")
    ResponseEntity<?> deleteValue(@PathVariable("value-id") Long valueId) {
        try {
            valueService.delete(valueId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Deleted value successfully",
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

    @PutMapping("/{value-id}")
    ResponseEntity<?> updateValue(@PathVariable("value-id") Long valueId,
                                  @RequestBody ProductOptionValueModel valueModel) {
        try {
            valueService.update(valueId, valueModel);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Updated value successfully",
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
