package org.example.final_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.model.enum_status.ActivateStatus;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.*;
import org.example.final_project.util.ConvertJsonObject;
import org.example.final_project.util.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.final_project.util.ConvertJsonObject.convertJsonToOption;
import static org.example.final_project.util.specification.ProductSpecification.*;

@Service
public class ProductService implements IProductService {
    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    IImageProductService imageService;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    IProductOptionService optionService;


    @Override
    public List<ProductDto> getAll() {
        return iProductRepository.findAll().stream().filter(x -> x.getIsActive() == 1 && x.getDeletedAt() == null).map(productMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        if (iProductRepository.findById(id).isPresent()) {
            return productMapper.convertToDto(iProductRepository.findById(id).get());
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
            ProductEntity productEntity = productMapper.convertToEntity(productModel);
            if (iProductRepository.findById(aLong).isPresent()) {
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
            for (MultipartFile file : productModel.getFiles()) {
                imageService.save(new ImageProductModel(file, savedProduct.getId()));
            }
            return (int) savedProduct.getId();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int deactivateProduct(long id, int type, String note) {
        try {
            ProductEntity productEntity = iProductRepository.findById(id).get();
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
    public Page<ProductDto> findAllByPage(Pageable pageable) {
        try {
            if (pageable != null) {
                return iProductRepository.findAll(Specification.where(isNotDeleted()), pageable).map(x -> productMapper.convertToDto(x));
            } else {
                return iProductRepository.findAll(Specification.where(isNotDeleted()), PageRequest.of(0, iProductRepository.findAll().size())).map(x -> productMapper.convertToDto(x));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductDto> findAllByNameAndPage(String name, Pageable pageable) {
        try {
            if (pageable != null) {
                return iProductRepository.findAll(Specification.where(isNotDeleted().and(hasName(name))), pageable).map(x -> productMapper.convertToDto(x));
            } else {
                return iProductRepository.findAll(Specification.where(isNotDeleted().and(hasName(name))), PageRequest.of(0, iProductRepository.findAll().size())).map(x -> productMapper.convertToDto(x));
            }
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public Page<ProductDto> getAllProductByStatus(int status, Pageable pageable) {
        if (pageable != null) {
            if (ActivateStatus.Active.checkIfExist(status)) {
                return iProductRepository.findAll(Specification.where(isStatus(status)).and(isNotDeleted()), pageable).map(x -> productMapper.convertToDto(x));
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } else {
            if (ActivateStatus.Active.checkIfExist(status)) {
                return iProductRepository.findAll(Specification.where(isStatus(status)).and(isNotDeleted()), PageRequest.of(0, iProductRepository.findAll().size())).map(x -> productMapper.convertToDto(x));
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        }
    }

    @Override
    public Page<ProductDto> getAllProductRelative(long productId, Pageable pageable) {
        try {
            if (pageable != null) {
                if (iProductRepository.findById(productId).isPresent()) {
                    ProductEntity productEntity = iProductRepository.findById(productId).get();
                    Page<ProductDto> page = iProductRepository.findAll(Specification.where(isNotDeleted()).and(hasCategoryId(productEntity.getCategoryEntity().getId()).and(hasUserNotDeleted(productEntity.getUser().getUserId()))).and(notHaveId(productId)), pageable).map(x -> productMapper.convertToDto(x));
                    return page;
                } else {
                    throw new IllegalArgumentException("Value Not Found");
                }
            } else {
                ProductEntity productEntity = iProductRepository.findById(productId).get();
                return iProductRepository.findAll(Specification.where(isNotDeleted()).and(hasCategoryId(productEntity.getCategoryEntity().getId())).and(notHaveId(productId).and(hasUserNotDeleted(productEntity.getUser().getUserId()))), Pageable.unpaged()).map(x -> productMapper.convertToDto(x));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductDto> getOtherProductOfShop(long productId, Pageable pageable) {
        try {
            if (iProductRepository.findById(productId).isPresent()) {
                ProductEntity productEntity = iProductRepository.findById(productId).get();
                if (pageable != null) {
                    return iProductRepository.findAll(Specification.where(isNotDeleted()).and(notHaveId(productId)).and(hasUserId(productEntity.getUser().getUserId())).and(hasUserNotDeleted(productEntity.getUser().getUserId())), pageable).map(x -> productMapper.convertToDto(x));
                } else {
                    return iProductRepository.findAll(Specification.where(isNotDeleted()).and(notHaveId(productId)).and(hasUserId(productEntity.getUser().getUserId())).and(hasUserNotDeleted(productEntity.getUser().getUserId())), Pageable.unpaged()).map(x -> productMapper.convertToDto(x));
                }
            } else {
                throw new IllegalArgumentException("Value Not Found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductDto> getAllProductOfShop(long userId, Pageable pageable) {
        try {
            if (iUserRepository.findById(userId).isPresent()) {
                if (pageable != null) {
                    return iProductRepository.findAll(Specification.where(isNotDeleted()).and(hasUserId(userId).and(hasUserNotDeleted(userId))), pageable).map(x -> productMapper.convertToDto(x));
                } else {
                    return iProductRepository.findAll(Specification.where(isNotDeleted()).and(hasUserId(userId).and(hasUserNotDeleted(userId))), Pageable.unpaged()).map(x -> productMapper.convertToDto(x));
                }
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Page<ProductDto> getAllProductByCategory(long categoryId, Pageable pageable) {
        if (pageable != null) {
            return iProductRepository.findAll(Specification.where(hasCategory(categoryId)).and(isNotDeleted()),pageable).map(x->productMapper.convertToDto(x));
        }else{
            return iProductRepository.findAll(Specification.where(hasCategory(categoryId)).and(isNotDeleted()),Pageable.unpaged()).map(x->productMapper.convertToDto(x));
        }
    }

    @Override
    public Page<ProductDto> getAllProductByPrice(double startPrice, double endPrice, Pageable pageable) {
        return null;
    }

}
