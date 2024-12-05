package org.example.final_project.controller;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageUploader {
    @Autowired
    Cloudinary cloudinary;

    @PostMapping("/upload")
    ResponseEntity uploadImage(@ModelAttribute MultipartFile[] files){
        return null;
    }
}
