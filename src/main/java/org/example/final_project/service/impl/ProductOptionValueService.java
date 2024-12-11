package org.example.final_project.service.impl;

import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.mapper.ProductOptionValueMapper;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.model.SKUModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.service.IProductService;
import org.example.final_project.service.ISKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductOptionValueService implements IProductOptionValueService {
    @Autowired
    IProductOptionValueRepository valueRepository;
    @Autowired
    ProductOptionValueMapper mapper;
    @Autowired
    IProductOptionRepository optionRepository;
    @Autowired
    ProductOptionValueMapper valueMapper;
    @Autowired
    ISKUService iskuService;
    @Autowired
    IProductRepository productRepository;

    @Override
    public List<ProductOptionValueDto> getAll() {
        return valueRepository.findAll().stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public ProductOptionValueDto getById(Long id) {
        try {
            if (valueRepository.findById(id).isPresent()) {
                return mapper.convertToDto(valueRepository.findById(id).get());
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int save(ProductOptionValueModel productOptionValueModel) {
        ProductOptionValuesEntity value = valueMapper.convertToEntity(productOptionValueModel);
        value.setOption(optionRepository.findById(productOptionValueModel.getOptionId()).get());
        valueRepository.save(value);
        return 1;
    }

    @Override
    public int update(Long aLong, ProductOptionValueModel productOptionValueModel) {
        try {
            if (valueRepository.findById(aLong).isPresent()) {
                ProductOptionValuesEntity values = mapper.convertToEntity(productOptionValueModel);
                values.setId(aLong);
                valueRepository.save(values);
                return 1;
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int delete(Long id) {
        try {
            if (valueRepository.findById(id).isPresent()) {
                valueRepository.deleteById(id);
                return 1;
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int saveCustom(Long productId, ProductOptionValueModel valueModel) throws IOException {
        try {
            ProductOptionValuesEntity entity = mapper.convertToEntity(valueModel);
            entity.setOption(optionRepository.findById(valueModel.getOptionId()).get());
            ProductOptionValuesEntity savedValue = valueRepository.save(entity);
            if (productRepository.findById(productId).isPresent()) {
                Set<Long> optionList = iskuService.getAllOptionOfProduct(productId);
                for (Long optionId : optionList) {
                    if (optionId != valueModel.getOptionId()) {
                        ProductOptionsEntity option = optionRepository.findById(optionId).get();
                        for (ProductOptionValuesEntity value : option.getValuesEntities()) {
                            SKUModel skuModel = new SKUModel();
                            skuModel.setProductId(productId);
                            skuModel.setOptionId1(valueModel.getOptionId());
                            skuModel.setValueId1(savedValue.getId());
                            skuModel.setOptionId2(optionId);
                            skuModel.setValueId2(value.getId());
                            iskuService.saveCustom(skuModel);
                        }
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }
}
