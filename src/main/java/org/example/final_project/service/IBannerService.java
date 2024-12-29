package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.BannerDto;
import org.example.final_project.model.BannerModel;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IBannerService {
    ApiResponse<?> createBanner(BannerModel bannerModel) throws IOException;

    ApiResponse<?> choseImage(Long bannerId);

    ApiResponse<?> getAllBanners(Pageable pageable);

    BannerDto getBannerIsActive();

    List<BannerDto> getBannerByShopId(Long shopId);

    ApiResponse<?> deleteBanner(Long bannerId);


}
