package org.example.final_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductSummaryDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.enum_status.ActivateStatus;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.final_project.util.specification.ProductSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {
    IProductRepository iProductRepository;
    ProductMapper productMapper;
    IImageProductService imageService;
    IUserRepository iUserRepository;
    IProductOptionService optionService;


    @Override
    public List<ProductDto> getAll() {
        return iProductRepository.findAll().stream().filter(x -> x.getIsActive() == 1 && x.getDeletedAt() == null).map(productMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        Specification<ProductEntity> specification = Specification.where(
                hasId(id).and(isValid())
        );
        if (iProductRepository.findOne(specification).isPresent()) {
            return productMapper.convertToDto(iProductRepository.findOne(specification).get());
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int save(ProductModel productModel) {
        return 0;
    }

    @Override
    public int update(Long aLong, ProductModel productModel) {
        try {
            if (iProductRepository.findById(aLong).isPresent()) {
                ProductEntity productEntity = productMapper.convertToEntity(productModel);
                if (productModel.getFiles() != null && productModel.getFiles().length != 0) {
                    for (MultipartFile file : productModel.getFiles()) {
                        imageService.save(new ImageProductModel(file, aLong));
                    }
                }
                if (iUserRepository.findById(productModel.getUser_id()).isPresent()) {
                    productEntity.setUser(iUserRepository.findById(productModel.getUser_id()).get());
                }
                productEntity.setId(aLong);
                iProductRepository.save(productEntity);
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int delete(Long id) {
        try {
            ProductEntity productEntity = iProductRepository.findById(id).get();
            if (productEntity != null) {
                productEntity.setDeletedAt(LocalDateTime.now());
                iProductRepository.save(productEntity);
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int saveCustom(ProductModel productModel) throws JsonProcessingException {
        try {
            ProductEntity productEntity = productMapper.convertToEntity(productModel);
            productEntity.setIsActive(0);
            productEntity.setUser(iUserRepository.findById(productModel.getUser_id()).get());
            ProductEntity savedProduct = iProductRepository.save(productEntity);
            if (productModel.getFiles() != null && productModel.getFiles().length != 0) {
                for (MultipartFile file : productModel.getFiles()) {
                    imageService.save(new ImageProductModel(file, savedProduct.getId()));
                }
            }
            return (int) savedProduct.getId();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int deactivateProduct(long id, int type, String note) {
        try {
            ProductEntity productEntity = iProductRepository.findById(id).isPresent()
                    ? iProductRepository.findById(id).get()
                    : null;
            if (productEntity != null) {
                if (ActivateStatus.Active.checkIfExist(type)) {
                    productEntity.setIsActive(type);
                    productEntity.setNote(note);
                    iProductRepository.save(productEntity);
                }
            } else {
                throw new IllegalArgumentException("Value not found");
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductSummaryDto> findAllByPage(Pageable pageable) {
        try {
            return pageable != null
                    ? iProductRepository.findAll(Specification.where(isValid()), pageable).map(productMapper::toProductSummaryDto)
                    : iProductRepository.findAll(Specification.where(isValid()), Pageable.unpaged()).map(productMapper::toProductSummaryDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductSummaryDto> findAllByNameAndPage(String name, Pageable pageable) {
        try {
            return pageable != null
                    ? iProductRepository.findAll(Specification.where(isValid().and(hasName(name))), pageable).map(productMapper::toProductSummaryDto)
                    : iProductRepository.findAll(Specification.where(isValid().and(hasName(name))), Pageable.unpaged()).map(productMapper::toProductSummaryDto);
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public Page<ProductSummaryDto> getAllProductByStatus(int status, Pageable pageable) {
        if (pageable != null) {
            if (ActivateStatus.Active.checkIfExist(status)) {
                return iProductRepository.findAll(Specification.where(isStatus(status)).and(isValid()), pageable).map(productMapper::toProductSummaryDto);
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } else {
            if (ActivateStatus.Active.checkIfExist(status)) {
                return iProductRepository.findAll(Specification.where(isStatus(status)).and(isValid()), PageRequest.of(0, iProductRepository.findAll().size())).map(productMapper::toProductSummaryDto);
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        }
    }

    @Override
    public Page<ProductSummaryDto> getAllProductRelative(long productId, Pageable pageable) {
        try {
            if (pageable != null) {
                if (iProductRepository.findById(productId).isPresent()) {
                    ProductEntity productEntity = iProductRepository.findById(productId).get();
                    return iProductRepository.findAll(Specification.where(isValid()).and(hasCategoryId(productEntity.getCategoryEntity().getId()).and(hasUserNotDeleted(productEntity.getUser().getUserId()))).and(notHaveId(productId)), pageable).map(productMapper::toProductSummaryDto);
                } else {
                    throw new IllegalArgumentException("Value Not Found");
                }
            } else {
                ProductEntity productEntity = iProductRepository.findById(productId).get();
                return iProductRepository.findAll(Specification.where(isValid()).and(hasCategoryId(productEntity.getCategoryEntity().getId())).and(notHaveId(productId).and(hasUserNotDeleted(productEntity.getUser().getUserId()))), Pageable.unpaged()).map(productMapper::toProductSummaryDto);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductSummaryDto> getOtherProductOfShop(long productId, Pageable pageable) {
        try {
            if (iProductRepository.findById(productId).isPresent()) {
                ProductEntity productEntity = iProductRepository.findById(productId).get();
                if (pageable != null) {
                    return iProductRepository.findAll(Specification.where(isValid()).and(notHaveId(productId)).and(hasUserId(productEntity.getUser().getUserId())).and(hasUserNotDeleted(productEntity.getUser().getUserId())), pageable).map(productMapper::toProductSummaryDto);
                } else {
                    return iProductRepository.findAll(Specification.where(isValid()).and(notHaveId(productId)).and(hasUserId(productEntity.getUser().getUserId())).and(hasUserNotDeleted(productEntity.getUser().getUserId())), Pageable.unpaged()).map(productMapper::toProductSummaryDto);
                }
            } else {
                throw new IllegalArgumentException("Value Not Found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductSummaryDto> getAllProductOfShop(long userId, Pageable pageable) {
        try {
            if (iUserRepository.findById(userId).isPresent()) {
                Specification<ProductEntity> specification = Specification.where(isValid())
                        .and(hasUserId(userId));

                return pageable != null
                        ? iProductRepository.findAll(specification, pageable).map(productMapper::toProductSummaryDto)
                        : iProductRepository.findAll(specification, Pageable.unpaged()).map(productMapper::toProductSummaryDto);
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public Page<ProductSummaryDto> getAllProductByCategory(long categoryId, Pageable pageable) {
        return pageable != null
                ? iProductRepository.findAll(Specification.where(hasCategoryId(categoryId)).and(isValid()), pageable).map(productMapper::toProductSummaryDto)
                : iProductRepository.findAll(Specification.where(hasCategoryId(categoryId)).and(isValid()), Pageable.unpaged()).map(productMapper::toProductSummaryDto);
    }

    @Override
    public Page<ProductSummaryDto> getAllProductByFilter(List<Long> categoryId, List<Long> addressId, Double startPrice, Double endPrice, Double rating, Pageable pageable) {
        Specification<ProductEntity> filter = Specification.where(isValid()).and(isStatus(1));
        if (categoryId != null) {
            filter = filter.and(hasCategory(categoryId));
        }
        if (addressId != null) {
            filter = filter.and(hasShopAddress(addressId));
        }
        if (startPrice != null && endPrice != null) {
            filter = filter.and(hasPriceBetween(startPrice, endPrice));
        }
        if (rating != null) {
            filter = filter.and(hasAverageRatingGreaterThan(rating));
        }
        return iProductRepository.findAll(filter, pageable).map(productMapper::toProductSummaryDto);
    }
}
