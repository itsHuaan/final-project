package org.example.final_project.service.impl;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IImageProductService;
import org.example.final_project.service.IProductService;
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

import static org.example.final_project.util.specification.ProductSpecification.*;

@Service
public class ProductService implements IProductService {
    @Autowired
    IProductRepository iProductRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    IImageProductService imageService;

    @Override
    public List<ProductDto> getAll() {
        return iProductRepository.findAll().stream().filter(x -> x.getIsActive() == 1 && x.getDeletedAt() == null).map(x -> productMapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        if (iProductRepository.findById(id).get() != null) {
            return productMapper.convertToDto(iProductRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public int save(ProductModel productModel) {
        try {
            ProductEntity productEntity = productMapper.convertToEntity(productModel);
            ProductEntity savedProduct = iProductRepository.save(productEntity);
            for (MultipartFile file : productModel.getFiles()) {
                imageService.save(new ImageProductModel(file, savedProduct.getId(), 1));
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int update(Long aLong, ProductModel productModel) {
        try {
            ProductEntity productEntity = productMapper.convertToEntity(productModel);
            if (iProductRepository.findById(aLong).get() != null) {
                productEntity.setId(aLong);
                iProductRepository.save(productEntity);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
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
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int inActivateProduct(long id) {
        try {
            ProductEntity productEntity = iProductRepository.findById(id).get();
            if (productEntity != null) {
                productEntity.setIsActive(0);
                iProductRepository.save(productEntity);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public Page<ProductDto> findAllByPage(Pageable pageable) {
        if (pageable != null) {
            return iProductRepository.findAll(Specification.where(isActive().and(isNotDeleted())),pageable).map(x -> productMapper.convertToDto(x));
        } else {
            return iProductRepository.findAll(Specification.where(isActive().and(isNotDeleted())),PageRequest.of(0, iProductRepository.findAll().size())).map(x->productMapper.convertToDto(x));
        }
    }

    @Override
    public Page<ProductDto> findAllByNameAndPage(String name, Pageable pageable) {
        if(pageable!=null){
            return iProductRepository.findAll(Specification.where(isActive().and(isNotDeleted()).and(hasName(name))),pageable).map(x->productMapper.convertToDto(x));
        }else{
            return iProductRepository.findAll(Specification.where(isActive().and(isNotDeleted()).and(hasName(name))),PageRequest.of(0,iProductRepository.findAll().size())).map(x->productMapper.convertToDto(x));
        }
    }
}
