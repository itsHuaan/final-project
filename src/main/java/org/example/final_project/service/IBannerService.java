package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.BannerDto;
import org.example.final_project.model.BannerModel;
import org.example.final_project.model.ImageActive;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IBannerService {
    int createBanner(BannerModel bannerModel) throws IOException;

    int choseImage( Long bannerId);
    ApiResponse<?> getAllBanners(Pageable pageable);

    BannerDto getBannerIsActive();
    List<BannerDto> getBannerByShopId(Long shopId);


}
