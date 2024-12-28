package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.service.IImageProductService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@Tag(name = "Image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(Const.API_PREFIX + "/image")
public class ImageController {
    IImageProductService imageProductService;
    MediaUploadService mediaUploadService;

    @PostMapping("/upload")
    ResponseEntity<?> uploadImage(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Upload image successfully",
                mediaUploadService.uploadSingleMediaFile(file)
        ));
    }

    @PostMapping("/chat/upload")
    public ResponseEntity<?> uploadMedia(@RequestParam("files") MultipartFile[] files) {
        try {
            if (files.length == 0) {
                return ResponseEntity.badRequest().body(List.of("No files provided"));
            }
            List<String> mediaUrls = mediaUploadService.uploadMultipleMediaFiles(files);
            return ResponseEntity.ok(mediaUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Failed to upload files: " + e.getMessage()));
        }
    }

    @DeleteMapping("/product/{image-id}")
    ResponseEntity<?> deleteProductImage(@PathVariable("image-id") long imageProductId) {
        try {
            imageProductService.delete(imageProductId);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Deleted image successfully",
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
