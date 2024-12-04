package org.example.final_project.service.impl;

import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.mapper.ProductOptionMapper;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOptionService implements IProductOptionService {
    @Autowired
    IProductOptionRepository optionRepository;
    @Autowired
    ProductOptionMapper mapper;
    @Autowired
    IProductOptionValueService valueService;
    @Autowired
    IProductRepository productRepository;
    @Override
    public List<ProductOptionDto> getAll() {
        return optionRepository.findAll().stream().map(x->mapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public ProductOptionDto getById(Long id) {
        if(optionRepository.findById(id).isPresent()){
            return mapper.convertToDto(optionRepository.findById(id).get());
        }else{
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int save(ProductOptionsModel model) {
        try{
            ProductOptionsEntity entity=mapper.convertToEntity(model);
            entity.setProduct(productRepository.findById(model.getProductId()).get());
            ProductOptionsEntity savedOption=optionRepository.save(entity);
            for (ProductOptionValueModel value:model.getValues()){
                valueService.save(new ProductOptionValueModel(value.getName(),savedOption.getId()));
            }
            return 1;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public int update(Long aLong, ProductOptionsModel model) {
        try{
            if(optionRepository.findById(aLong).isPresent()){
                ProductOptionsEntity entity=mapper.convertToEntity(model);
                entity.setId(aLong);
                optionRepository.save(entity);
                return 1;
            }else{
                throw new IllegalArgumentException("Value not found");
            }
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public int delete(Long id) {
        try{
            if(optionRepository.findById(id).isPresent()){
                optionRepository.deleteById(id);
                return 1;
            }else{
                throw new IllegalArgumentException("Value not found");
            }
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<ProductOptionDto> getAllByProduct(long productId) {
        return optionRepository.findAllByProduct_Id(productId).stream().map(x->mapper.convertToDto(x)).collect(Collectors.toList());
    }
}
