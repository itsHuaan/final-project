package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.BannerDto;
import org.example.final_project.model.BannerModel;
import org.example.final_project.model.ImageActive;
import org.example.final_project.service.IBannerService;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/banner")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Banner")
public class BannerController {

    IBannerService bannerService;

    @PostMapping("")
    public ResponseEntity<String> createBanner(BannerModel bannerModel) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(bannerService.createBanner(bannerModel) == 1 ? "đã thêm " : "chưa thêm");
    }

    @GetMapping("")
    public ResponseEntity<?> getBanner(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        Pageable pageable = PageableValidation.setDefault(size, page);
        return ResponseEntity.status(HttpStatus.OK).body(bannerService.getAllBanners(pageable));
    }

    @PutMapping("")
    public ResponseEntity<String> updateBanner(@RequestBody ImageActive imageActive) {
        return ResponseEntity.status(HttpStatus.OK).body(bannerService.choseImage(imageActive) == 1 ? "đã chọn thành công " : "không thể chọn ");
    }

    @GetMapping("/active")
    public ResponseEntity<?> getBannerIsActive() {
        BannerDto banner;
        HttpStatus httpStatus;
        String message;
        try {
            banner = bannerService.getBannerIsActive();
            httpStatus = HttpStatus.OK;
            message = "Banner fetched";
        } catch (IllegalArgumentException e){
            banner = null;
            httpStatus = HttpStatus.NOT_FOUND;
            message = "Banner not found";
        }
        return ResponseEntity.status(httpStatus).body(
                createResponse(
                        httpStatus,
                        message,
                        banner
                )
        );
    }


}
