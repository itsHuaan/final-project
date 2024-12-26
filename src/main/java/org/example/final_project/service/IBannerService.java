package org.example.final_project.service;

import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.BannerDto;
import org.example.final_project.model.BannerModel;
import org.example.final_project.model.ImageActive;

import java.io.IOException;

public interface IBannerService {
    int createBanner(BannerModel bannerModel) throws IOException;

    int choseImage(ImageActive imageActive);

    ApiResponse<?> getAllBanners(Integer page, Integer size);

    BannerDto getBannerIsActive();


}
