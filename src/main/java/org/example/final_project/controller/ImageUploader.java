package org.example.final_project.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
public class ImageUploader {
    @Autowired
    Cloudinary cloudinary;

    @PostMapping("/upload")
    ResponseEntity uploadImage(MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Successfully",
                cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString()
        ));
    }
}
