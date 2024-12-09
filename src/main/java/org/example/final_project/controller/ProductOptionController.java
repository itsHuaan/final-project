package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX+"/option")
@Tag(name="Product-Option-Controller")
public class ProductOptionController {
    @Autowired
    IProductOptionService optionService;
    @Autowired
    ISKUService iskuService;
    @PostMapping("/create-new")
    ResponseEntity addNewOption(@RequestBody ProductOptionsModel model){
        try{
            optionService.save(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(
                    HttpStatus.CREATED,
                    "Successfully",
                    null
            ));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.CREATED,
                    e.getMessage(),
                    null
            ));
        }
    }

}
