package org.example.final_project.service.impl;

import org.example.final_project.dto.ProductOptionValuesDto;
import org.example.final_project.mapper.ProductOptionValuesMapper;
import org.example.final_project.model.ProductOptionsValueModel;
import org.example.final_project.repository.IProductOptionValuesRepository;
import org.example.final_project.service.IProductOptionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOptionValuesService implements IProductOptionValueService {
    @Autowired
    IProductOptionValuesRepository valuesRepository;
    @Autowired
    ProductOptionValuesMapper valuesMapper;
    @Override
    public List<ProductOptionValuesDto> getAll() {
        return null;
    }

    @Override
    public ProductOptionValuesDto getById(Long id) {
        return null;
    }

    @Override
    public int save(ProductOptionsValueModel productOptionsValueModel) {
        try{
            valuesRepository.save(valuesMapper.convertToEntity(productOptionsValueModel));
            return 1;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int update(Long aLong, ProductOptionsValueModel productOptionsValueModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
