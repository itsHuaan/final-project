package org.example.final_project.mapper;

import org.example.final_project.dto.BannerDto;
import org.example.final_project.entity.BannerEntity;
import org.example.final_project.model.BannerModel;

import java.time.LocalDateTime;

public class BannerMapper {
    public static BannerEntity toBannerEntity(BannerModel bannerModel) {
        return BannerEntity.builder()
                .createEnd(bannerModel.getCreateEnd())
                .createStart(bannerModel.getCreateStart())
                .price(bannerModel.getPrice())
                .timeCreate(LocalDateTime.now())
                .shopId(bannerModel.getShopId())
                .build();
    }

    public static BannerDto toBannerDto(BannerEntity bannerEntity) {
        return BannerDto.builder()
                .createEnd(bannerEntity.getCreateEnd())
                .createStart(bannerEntity.getCreateStart())
                .price(bannerEntity.getPrice())
                .timeCreate(bannerEntity.getTimeCreate())
                .shopId(bannerEntity.getShopId())
                .id(bannerEntity.getId())
                .image(bannerEntity.getImage())
                .isActive(bannerEntity.getIsActive())
                .build();
    }
}
