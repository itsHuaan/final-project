package org.example.final_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.example.final_project.util.ConvertJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return optionRepository.findAll().stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public ProductOptionDto getById(Long id) {
        if (optionRepository.findById(id).isPresent()) {
            return mapper.convertToDto(optionRepository.findById(id).get());
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int save(ProductOptionsModel model) {
        return 0;
    }

    @Override
    public int update(Long aLong, ProductOptionsModel model) {
        try {
            if (optionRepository.findById(aLong).isPresent()) {
                ProductOptionsEntity entity = mapper.convertToEntity(model);
                entity.setId(aLong);
                optionRepository.save(entity);
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
            if (optionRepository.findById(id).isPresent()) {
                optionRepository.deleteById(id);
                return 1;
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public List<ProductOptionDto> saveAllOption(List<String> jsonOptions) throws JsonProcessingException {
        try {
            List<ProductOptionsEntity> list = new ArrayList<>();
            if (jsonOptions != null && jsonOptions.size() != 0) {
                for (ProductOptionsModel model : ConvertJsonObject.convertJsonToOption(jsonOptions)) {
                    list.add(saveCustom(model));
                }
            }else{
                ProductOptionsModel optionsModel=new ProductOptionsModel();
                optionsModel.setName("default");
                ProductOptionValueModel valueModel=new ProductOptionValueModel();
                valueModel.setName("default");
                optionsModel.setValues(List.of(valueModel));
                list.add(saveCustom(optionsModel));
            }
            return list.stream().map(x -> mapper.convertToDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ProductOptionsEntity saveCustom(ProductOptionsModel model) {
        try {
            ProductOptionsEntity entity = mapper.convertToEntity(model);
            ProductOptionsEntity savedOption = optionRepository.save(entity);
            for (ProductOptionValueModel value : model.getValues()) {
                valueService.save(new ProductOptionValueModel(value.getName(), savedOption.getId()));
            }
            return savedOption;
        } catch (Exception e) {
            throw e;
        }
    }
}
