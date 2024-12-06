package org.example.final_project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.dto.ProductOptionValueDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.mapper.SKUMapper;
import org.example.final_project.model.SKUModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.ISKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SKUService implements ISKUService {
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    ISKURepository iskuRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IProductOptionRepository optionRepository;
    @Autowired
    IProductOptionValueRepository valueRepository;
    @Autowired
    SKUMapper skuMapper;

    @Override
    public List<SKUDto> getAll() {
        try {
            return iskuRepository.findAll().stream().map(x -> skuMapper.convertToDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public SKUDto getById(Long id) {
        try {
            if (iskuRepository.findById(id).isPresent()) {
                return skuMapper.convertToDto(iskuRepository.findById(id).get());
            } else {
                throw new IllegalArgumentException("Value not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int save(SKUModel model) {
        return 0;
    }

    @Override
    public int update(Long aLong, SKUModel model) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }


    @Override
    public List<SKUDto> getAllByProduct(long productId) {
        return iskuRepository.findAllByProduct_Id(productId).stream().map(x -> skuMapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public SKUDto saveCustom(SKUModel model) throws IOException {
        SKUEntity entity = skuMapper.convertToEntity(model);
        if (productRepository.findById(model.getProductId()).isPresent() && optionRepository.findById(model.getOptionId()).isPresent() && valueRepository.findById(model.getValueId()).isPresent()) {
            entity.setProduct(productRepository.findById(model.getProductId()).get());
            entity.setOption(optionRepository.findById(model.getOptionId()).get());
            entity.setValue(valueRepository.findById(model.getValueId()).get());
        } else {
            throw new IllegalArgumentException("Value not found");
        }
        return skuMapper.convertToDto(iskuRepository.save(entity));
    }

    @Override
    public List<SKUDto> addListSKU(long productId, List<ProductOptionDto> optionList) throws IOException {
        try{
            List<SKUDto> stockList=new ArrayList<>();
            for (ProductOptionDto option:optionList){
                for(ProductOptionValueDto value:option.getValues()){
                    SKUModel skuModel=new SKUModel();
                    skuModel.setProductId(productId);
                    skuModel.setOptionId(option.getId());
                    skuModel.setValueId(value.getId());
                    stockList.add(saveCustom(skuModel));
                }
            }
            return stockList;
        }catch(Exception e){
            throw e;
        }
    }


}
