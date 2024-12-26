package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/value")
@Tag(name = "Product Option Value")
public class ProductOptionValueController {
    @Autowired
    IProductOptionValueService valueService;

    @PostMapping("/{product-id}")
    ResponseEntity<?> addNewValue(@PathVariable("product-id") Long productId,
                                  ProductOptionValueModel valueModel) {
        try {
            valueService.saveCustom(productId, valueModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
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

    @DeleteMapping("/{value-id}")
    ResponseEntity<?> deleteValue(@PathVariable("value-id") Long valueId) {
        try {
            valueService.delete(valueId);
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

    @PutMapping("/{value-id}")
    ResponseEntity<?> updateValue(@PathVariable("value-id") Long valueId,
                                  @RequestBody ProductOptionValueModel valueModel) {
        try {
            valueService.update(valueId, valueModel);
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
}
