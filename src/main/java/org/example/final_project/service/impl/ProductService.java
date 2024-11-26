package org.example.final_project.service.impl;

import org.example.final_project.dto.ProductDto;
import org.example.final_project.entity.ImageProduct;
import org.example.final_project.entity.Product;
import org.example.final_project.mapper.ProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IImageProductService;
import org.example.final_project.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return iProductRepository.findAll().stream().filter(x->x.isActive() && x.getDeletedAt()==null).map(x -> productMapper.convertToDto(x)).collect(Collectors.toList());
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
            Product product=iProductRepository.save(productMapper.convertToEntity(productModel));
            for (MultipartFile file:productModel.getFiles()){
                imageService.save(new ImageProductModel(file,product.getId()));
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
            Product product = productMapper.convertToEntity(productModel);
            if (iProductRepository.findById(aLong).get() != null) {
                product.setId(aLong);
                iProductRepository.save(product);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        try{
            Product product=iProductRepository.findById(id).get();
            if (product!=null){
                product.setDeletedAt(LocalDateTime.now());
                iProductRepository.save(product);
            }
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int inActivateProduct(long id) {
        try{
            Product product=iProductRepository.findById(id).get();
            if (product!=null){
                product.setActive(false);
                iProductRepository.save(product);
            }
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public Page<ProductDto> findAllByPage(Pageable pageable) {
        return new PageImpl<>(iProductRepository.findAll().stream().filter(x->x.isActive()).map(x->productMapper.convertToDto(x)).collect(Collectors.toList()),pageable,iProductRepository.findAll(pageable).getTotalElements());
    }
}
