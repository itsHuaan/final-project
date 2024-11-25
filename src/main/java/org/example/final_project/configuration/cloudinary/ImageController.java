package org.example.final_project.configuration.cloudinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    @Autowired
    ImageService imageService;
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile[] file) {
        try {
            // Gọi dịch vụ upload ảnh
            String imageUrl = imageService.uploadImage(file);
            return imageUrl;  // Trả về URL ảnh đã upload
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading image: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error occurred: " + e.getMessage();
        }
    }

}
