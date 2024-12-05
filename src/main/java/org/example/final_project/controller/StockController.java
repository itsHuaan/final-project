package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.model.SKUModel;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/stock")
@Tag(name = "Stock Controller")
public class StockController {
    @Autowired
    ISKUService skuService;

    @GetMapping("/{product-id}")
    ResponseEntity getAllStockByProduct(@PathVariable("product-id") long productId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    skuService.getAllByProduct(productId)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping("/create-new")
    ResponseEntity addNewStock(@ModelAttribute SKUModel model) {
        try {
            skuService.saveCustom(model);
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
}
