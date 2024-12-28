package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.PromotionDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.PromotionEntity;
import org.example.final_project.mapper.PromotionMapper;
import org.example.final_project.model.PromotionModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IPromotionRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IPromotionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    IUserRepository iUserRepository;

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
        if (iPromotionRepository.findById(id).isPresent()) {
            PromotionEntity promotion = iPromotionRepository.findById(id).get();
            promotion.setDeletedAt(LocalDateTime.now());
            iPromotionRepository.save(promotion);
            return 1;
        } else {
            throw new IllegalArgumentException("Promotion is not present");
        }
    }

    @Override
    public Page<PromotionDto> findAllByPage(Pageable pageable) {
        return iPromotionRepository.findAll(Specification.where(isNotDeleted()), pageable).map(promotionMapper::convertToDto);
    }

    @Override
    public int applyPromotion(Long promotionId, List<Long> productIds) {
        if (iPromotionRepository.findById(promotionId).isPresent()) {
            PromotionEntity promotion = iPromotionRepository.findById(promotionId).get();
            for (Long productId : productIds) {
                if (productRepository.findById(productId).isPresent()) {
                    if (!promotion.getProducts().contains(productRepository.findById(productId).get())) {
                        ProductEntity product = productRepository.findById(productId).get();
                        product.getPromotions().add(promotion);
                        productRepository.save(product);
                    }
                } else {
                    throw new IllegalArgumentException("Product is not present");
                }
            }
        } else {
            throw new IllegalArgumentException("Promotion is not present");
        }
        return 1;
    }

    @Override
    public PromotionEntity findAllPromotionByNow(Long productId) {
        List<PromotionEntity> promotionList = iPromotionRepository.findAll(isActiveForTheProduct(productId).and(isNotDeleted()));
        if (!promotionList.isEmpty()) {
            PromotionEntity maxPercentage = promotionList.stream().max(Comparator.comparing(PromotionEntity::getDiscountPercentage)).get();
            iPromotionRepository.save(maxPercentage);
            return maxPercentage;
        } else {
            return null;
        }
    }


    @Override
    public int cancelPromotionOfProduct(Long promotionId, List<Long> productIds) {
        try {
            if (iPromotionRepository.findById(promotionId).isPresent()) {
                PromotionEntity promotion = iPromotionRepository.findById(promotionId).get();
                for (Long productId : productIds) {
                    if (productRepository.findById(productId).isPresent()) {
                        if (promotion.getProducts().contains(productRepository.findById(productId).get())) {
                            promotion.getProducts().remove(productRepository.findById(productId).get());
                            ProductEntity product = productRepository.findById(productId).get();
                            product.getPromotions().remove(promotion);
                            iPromotionRepository.save(promotion);
                            productRepository.save(product);
                        } else {
                            throw new IllegalArgumentException("Product is not in promotion");
                        }
                    } else {
                        throw new IllegalArgumentException("Product is not present");
                    }
                }
                return 1;
            } else {
                throw new IllegalArgumentException("Promotion is not present");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<PromotionDto> getAllByShop(Long shopId, Pageable pageable) {
        if (iUserRepository.findById(shopId).isPresent()) {
            return iPromotionRepository.findAll(isActiveOrComingForTheShop(shopId).and(isNotDeleted()), pageable).map(x -> promotionMapper.convertToDto(x));
        } else {
            throw new IllegalArgumentException("Shop is not present");
        }
    }
}
