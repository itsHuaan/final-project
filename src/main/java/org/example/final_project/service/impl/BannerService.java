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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerService implements IBannerService {
    MediaUploadService mediaUploadService;
    IBannerRepository bannerRepository;

    @Override
    public ApiResponse<?> createBanner(BannerModel bannerModel) throws IOException {
        if (bannerModel.getImage() == null || bannerModel.getImage().isEmpty()) {
            return ApiResponse.createResponse(HttpStatus.NOT_FOUND, "Image is null or empty", null);
        }

        try {
            String image = mediaUploadService.uploadOneImage(bannerModel.getImage());

            if (bannerModel.getCreateStart() == null || bannerModel.getCreateEnd() == null) {
                return ApiResponse.createResponse(HttpStatus.BAD_REQUEST, "Create start or end time is null", null);
            }

            if (bannerModel.getCreateStart().isAfter(bannerModel.getCreateEnd())) {
                return ApiResponse.createResponse(HttpStatus.BAD_REQUEST, "Create start time must be before create end time", null);
            }

            BannerEntity bannerEntity = BannerMapper.toBannerEntity(bannerModel);
            bannerEntity.setImage(image);
            bannerRepository.save(bannerEntity);

            return ApiResponse.createResponse(HttpStatus.OK, "Banner created", null);
        } catch (Exception e) {
            return ApiResponse.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading image or saving banner", null);
        }
    }


    @Override
    public ApiResponse<?> getAllBanners(Pageable pageable) {
        Page<BannerDto> bannerDtoPage = bannerRepository.findBannerPage(pageable).map(BannerMapper::toBannerDto);
        return ApiResponse.createResponse(HttpStatus.OK, "get all banners", bannerDtoPage);
    }


    @Override
    public ApiResponse<?> choseImage(Long bannerId) {
        try {
            BannerEntity bannerEntity = bannerRepository.findById(bannerId).orElseThrow(() ->
                    new IllegalArgumentException("Banner not found")
            );

            if (bannerEntity.getCreateStart().isAfter(LocalDateTime.now())) {
                return ApiResponse.createResponse(HttpStatus.OK, "The banner cannot be activated because the start time has not yet arrived", null);
            }

            LocalDateTime endDate = bannerEntity.getCreateEnd();

            List<BannerEntity> list = bannerRepository.findAllBanner();

            list.forEach(banner -> {
                if (banner.getCreateEnd() != null && banner.getCreateStart().isBefore(endDate)) {
                    banner.setIsActive(StatusBanner.OUTDATED.getBanner());
                } else {
                    banner.setIsActive(0);
                }
            });

            bannerEntity.setIsActive(StatusBanner.ACTIVE.getBanner());

            bannerRepository.saveAll(list);
            bannerRepository.save(bannerEntity);

            return ApiResponse.createResponse(HttpStatus.OK, "Banner status updated successfully", null);
        } catch (IllegalArgumentException e) {
            return ApiResponse.createResponse(HttpStatus.NOT_FOUND, "Banner not found", null);
        } catch (Exception e) {
            return ApiResponse.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while updating the banner status", null);
        }
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

    @Transactional
    @Override
    public ApiResponse<?> deleteBanner(Long bannerId) {
        try {
            Optional<BannerEntity> bannerEntityOptional = bannerRepository.findById(bannerId);
            if (bannerEntityOptional.isPresent()) {
                BannerEntity bannerEntity = bannerEntityOptional.get();
                if (bannerEntity.getIsActive() == StatusBanner.ACTIVE.getBanner()) {
                    bannerRepository.delete(bannerEntityOptional.get());
                    bannerRepository.deactivateAllBanners();
                    return ApiResponse.createResponse(HttpStatus.OK, "Banner active deleted successfully", null);

                } else {
                    bannerRepository.delete(bannerEntityOptional.get());
                    return ApiResponse.createResponse(HttpStatus.OK, "Banner not active deleted successfully", null);
                }

            } else {
                return ApiResponse.createResponse(HttpStatus.NOT_FOUND, "Banner not found", null);
            }
        } catch (Exception e) {
            return ApiResponse.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the banner", null);
        }
    }

}
