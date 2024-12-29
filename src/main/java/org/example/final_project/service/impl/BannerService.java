package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.cloudinary.MediaUploadService;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.BannerDto;
import org.example.final_project.entity.BannerEntity;
import org.example.final_project.enumeration.StatusBanner;
import org.example.final_project.mapper.BannerMapper;
import org.example.final_project.model.BannerModel;
import org.example.final_project.repository.IBannerRepository;
import org.example.final_project.service.IBannerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerService implements IBannerService {
    MediaUploadService mediaUploadService;
    IBannerRepository bannerRepository;

    @Override
    public int createBanner(BannerModel bannerModel) throws IOException {
        String image = mediaUploadService.uploadOneImage(bannerModel.getImage());
        BannerEntity bannerEntity = BannerMapper.toBannerEntity(bannerModel);
        bannerEntity.setImage(image);
        bannerRepository.save(bannerEntity);
        return 1;
    }

    @Override
    public ApiResponse<?> getAllBanners(Pageable pageable) {
        Page<BannerDto> bannerDtoPage = bannerRepository.findBannerPage(pageable).map(BannerMapper::toBannerDto);
        return ApiResponse.createResponse(HttpStatus.OK, "get all banners", bannerDtoPage);
    }


    @Override
    public int choseImage(Long bannerId) {

        BannerEntity bannerEntity = bannerRepository.findById(bannerId).orElseThrow(() ->
                new IllegalArgumentException("Banner not found")
        );

        List<BannerEntity> list = bannerRepository.findAllBanner();
        list.forEach(banner -> banner.setIsActive(0));
        LocalDateTime endDate = bannerEntity.getCreateEnd();
        for (BannerEntity bannerEntity1 : list) {
            if (bannerEntity1.getCreateEnd().isAfter(endDate)) {
                bannerEntity1.setIsActive(StatusBanner.OUTDATED.getBanner());
            }
        }
        bannerRepository.saveAll(list);
        bannerEntity.setIsActive(StatusBanner.ACTIVE.getBanner());
        bannerRepository.save(bannerEntity);
        return 1;
    }


    @Override
    public BannerDto getBannerIsActive() {
        BannerEntity bannerEntity = bannerRepository.bannerIsActive().orElse(null);
        if (bannerEntity == null) {
            throw new IllegalArgumentException("banner not found");
        }
        return BannerMapper.toBannerDto(bannerEntity);
    }

    @Override
    public List<BannerDto> getBannerByShopId(Long shopId) {
        List<BannerEntity> bannerEntities = bannerRepository.listBannerByShopId(shopId);
        return bannerEntities.stream().map(BannerMapper::toBannerDto).toList();
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredBanners() {
        LocalDateTime now = LocalDateTime.now();
        List<BannerEntity> list = bannerRepository.findExpiredBanners(now);
        list.forEach(banner -> banner.setIsActive(StatusBanner.ExpiredBanner.getBanner()));
        bannerRepository.saveAll(list);
    }
}
