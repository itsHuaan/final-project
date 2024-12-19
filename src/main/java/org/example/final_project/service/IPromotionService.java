package org.example.final_project.service;

import org.example.final_project.dto.PromotionDto;
import org.example.final_project.entity.PromotionEntity;
import org.example.final_project.model.PromotionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPromotionService extends IBaseService<PromotionDto, PromotionModel, Long> {
    Page<PromotionDto> findAllByPage(Pageable pageable);

    int activate(Long promotionId, Integer type);

    int applyPromotion(Long promotionId, List<Long> productId);

    PromotionEntity findAllPromotionByNow(Long productId);

    Page<PromotionDto> findAllPromotionInAdminSeller(Pageable pageable);
}
