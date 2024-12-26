package org.example.final_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.mapper.ProductOptionMapper;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.ConvertJsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionService implements IProductOptionService {
    IProductOptionRepository optionRepository;
    ProductOptionMapper mapper;
    IProductOptionValueService valueService;
    ISKUService iskuService;

    @Override
    public List<ProductOptionDetailDto> getAll() {
        return optionRepository.findAll().stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductOptionDetailDto getById(Long id) {
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
        if (optionRepository.findById(aLong).isPresent()) {
            ProductOptionsEntity entity = optionRepository.findById(aLong).get();
            entity.setName(model.getName());
            optionRepository.save(entity);
            return 1;
        } else {
            throw new IllegalArgumentException("Option is not found");
        }
    }

    @Override
    public int delete(Long id) {
        if (optionRepository.findById(id).isPresent()) {
            optionRepository.deleteById(id);
            return 1;
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }


    @Override
    public List<ProductOptionDetailDto> saveAllOption(String jsonOptions) throws JsonProcessingException {
        List<ProductOptionsEntity> list = new ArrayList<>();
        if (jsonOptions != null) {
            for (ProductOptionsModel model : ConvertJsonObject.convertJsonToOption(jsonOptions)) {
                list.add(saveCustom(model));
            }
        } else {
            ProductOptionsModel optionsModel = new ProductOptionsModel();
            optionsModel.setName("default");
            ProductOptionValueModel valueModel = new ProductOptionValueModel();
            valueModel.setName("default");
            optionsModel.setValues(List.of(valueModel));
            list.add(saveCustom(optionsModel));
        }
        return list.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductOptionsEntity saveCustom(ProductOptionsModel model) {
        ProductOptionsEntity entity = mapper.convertToEntity(model);
        ProductOptionsEntity savedOption = optionRepository.save(entity);
        for (ProductOptionValueModel value : model.getValues()) {
            valueService.save(new ProductOptionValueModel(value.getName(), savedOption.getId()));
        }
        return savedOption;
    }

    @Override
    public int getNumberOfOptionByProduct(Long productId) {
        Set<Long> optionIds = new HashSet<>();
        for (SKUDto sku : iskuService.getAllByProduct(productId)) {
            if (sku.getOption1() != null) {
                optionIds.add(sku.getOption1().getOptionId());
            }
            if (sku.getOption2() != null) {
                optionIds.add(sku.getOption2().getOptionId());
            }
        }
        return optionIds.size();
    }
}
