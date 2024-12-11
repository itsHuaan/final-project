package org.example.final_project.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.service.IImageProductService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@Tag(name="Image")
@RequestMapping(Const.API_PREFIX + "/image")
public class ImageController {
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    IImageProductService imageProductService;

    @PostMapping("/upload")
    ResponseEntity uploadImage(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Successfully",
                cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString()
        ));
    }
    @DeleteMapping("/product/{image-id}")
    ResponseEntity deleteProductImage(@PathVariable("image-id")long imageProductId){
        try{
            imageProductService.delete(imageProductId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Successfully",
                    null
            ));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }
}
