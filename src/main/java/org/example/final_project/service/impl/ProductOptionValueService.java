package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.mapper.ProductOptionValueMapper;
import org.example.final_project.model.ProductOptionValueModel;
import org.example.final_project.model.SKUModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.service.ISKUService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionValueService implements IProductOptionValueService {
    IProductOptionValueRepository valueRepository;
    IProductOptionRepository optionRepository;
    ProductOptionValueMapper valueMapper;
    @Lazy
    ISKUService iskuService;
    IProductRepository productRepository;

    @Override
    public List<ProductOptionValueDto> getAll() {
        return valueRepository.findAll().stream().map(valueMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductOptionValueDto getById(Long id) {
        if (valueRepository.findById(id).isPresent()) {
            return valueMapper.convertToDto(valueRepository.findById(id).get());
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int save(ProductOptionValueModel productOptionValueModel) {
        ProductOptionValuesEntity value = valueMapper.convertToEntity(productOptionValueModel);
        ProductOptionsEntity option = new ProductOptionsEntity();
        option.setId(productOptionValueModel.getOptionId());
        value.setOption(option);
        valueRepository.save(value);
        return 1;
    }

    @Override
    public int update(Long aLong, ProductOptionValueModel productOptionValueModel) {
        if (valueRepository.findById(aLong).isPresent()) {
            ProductOptionValuesEntity values = valueRepository.findById(aLong).get();
            values.setName(productOptionValueModel.getName());
            valueRepository.save(values);
            return 1;
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int delete(Long id) {
        if (valueRepository.findById(id).isPresent()) {
            valueRepository.deleteById(id);
            return 1;
        } else {
            throw new IllegalArgumentException("Value not found");
        }
    }

    @Override
    public int saveCustom(Long productId, ProductOptionValueModel valueModel) throws IOException {
        ProductOptionValuesEntity entity = valueMapper.convertToEntity(valueModel);
        entity.setOption(optionRepository.findById(valueModel.getOptionId()).get());
        ProductOptionValuesEntity savedValue = valueRepository.save(entity);
        if (productRepository.findById(productId).isPresent()) {
            Set<Long> optionList = iskuService.getAllOptionOfProduct(productId);
            Long optionId1 = optionList.stream().toList().get(0);
            if (optionList.size() == 2) {
                Long optionId2 = optionList.stream().toList().get(1);
                if (valueModel.getOptionId() == optionId1) {
                    for (ProductOptionValuesEntity value : valueRepository.findAllByOption_Id(optionId2)) {
                        SKUModel skuModel = new SKUModel();
                        skuModel.setProductId(productId);
                        skuModel.setOptionId1(optionId1);
                        skuModel.setValueId1(savedValue.getId());
                        skuModel.setOptionId2(optionId2);
                        skuModel.setValueId2(value.getId());
                        iskuService.saveCustom(skuModel);
                    }
                } else if (valueModel.getOptionId() == optionId2) {
                    for (ProductOptionValuesEntity value : valueRepository.findAllByOption_Id(optionId1)) {
                        SKUModel skuModel = new SKUModel();
                        skuModel.setProductId(productId);
                        skuModel.setOptionId1(optionId1);
                        skuModel.setValueId1(value.getId());
                        skuModel.setOptionId2(optionId2);
                        skuModel.setValueId2(savedValue.getId());
                        iskuService.saveCustom(skuModel);
                    }
                }
            } else {
                SKUModel skuModel = new SKUModel();
                skuModel.setProductId(productId);
                skuModel.setOptionId1(valueModel.getOptionId());
                skuModel.setValueId1(savedValue.getId());
                iskuService.saveCustom(skuModel);
            }
        } else {
            throw new IllegalArgumentException("Product not present");
        }
        return 1;
    }
}
