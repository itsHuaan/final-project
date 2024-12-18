package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.PromotionDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.PromotionEntity;
import org.example.final_project.mapper.PromotionMapper;
import org.example.final_project.model.PromotionModel;
import org.example.final_project.model.enum_status.PromotionStatus;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IPromotionRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static org.example.final_project.specification.PromotionSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionService implements IPromotionService {
    IPromotionRepository iPromotionRepository;
    PromotionMapper promotionMapper;
    IProductRepository productRepository;

    @Override
    public List<PromotionDto> getAll() {
        return null;
    }

    @Override
    public PromotionDto getById(Long id) {
        if (iPromotionRepository.findById(id).isPresent()) {
            return promotionMapper.convertToDto(iPromotionRepository.findById(id).get());
        } else {
            throw new IllegalArgumentException("Value not present");
        }
    }

    @Override
    public int save(PromotionModel model) {
        try {
            PromotionEntity promotion = promotionMapper.convertToEntity(model);
            promotion.setStatus(PromotionStatus.COMING_SOON.getValue());
            iPromotionRepository.save(promotion);
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int update(Long aLong, PromotionModel model) {
        if (iPromotionRepository.findById(aLong).isPresent()) {
            PromotionEntity promotionEntity = promotionMapper.convertToEntity(model);
            promotionEntity.setId(aLong);
            iPromotionRepository.save(promotionEntity);
            return 1;
        } else {
            throw new IllegalArgumentException("Value not present");
        }
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public Page<PromotionDto> findAllByPage(Pageable pageable) {
        return iPromotionRepository.findAll(pageable).map(promotionMapper::convertToDto);
    }

    @Override
    public int activate(Long promotionId, Integer type) {
        if (iPromotionRepository.findById(promotionId).isPresent()) {
            PromotionEntity promotion = iPromotionRepository.findById(promotionId).get();
            promotion.setStatus(type);
            iPromotionRepository.save(promotion);
            return 1;
        } else {
            throw new IllegalArgumentException("Value not present");
        }
    }

    @Override
    public int applyPromotion(Long promotionId, Long productId) {
        if (iPromotionRepository.findById(promotionId).isPresent()) {
            PromotionEntity promotion = iPromotionRepository.findById(promotionId).get();
            if (productRepository.findById(productId).isPresent()) {
                ProductEntity product = productRepository.findById(productId).get();
                product.getPromotions().add(promotion);
                productRepository.save(product);
            } else {
                throw new IllegalArgumentException("Product is not present");
            }
        } else {
            throw new IllegalArgumentException("Promotion is not present");
        }
        return 1;
    }

    @Override
    public PromotionEntity findAllPromotionByNow(Long productId) {
        List<PromotionEntity> promotionList = iPromotionRepository.findAll(isActiveButNotActivatedAndHaveProduct(productId).and(isNotDeleted()));
        if (!promotionList.isEmpty()) {
            PromotionEntity maxPercentage = promotionList.stream().max(Comparator.comparing(PromotionEntity::getDiscountPercentage)).get();
            maxPercentage.setStatus(PromotionStatus.ACTIVE.getValue());
            iPromotionRepository.save(maxPercentage);
            return maxPercentage;
        } else {
            return null;
        }
    }
}
