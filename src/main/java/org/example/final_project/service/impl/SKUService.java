package org.example.final_project.service.impl;

import com.cloudinary.Cloudinary;
import org.example.final_project.dto.OptionValueTemp;
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
    public SKUDto saveCustom(SKUModel model) {
        SKUEntity entity = skuMapper.convertToEntity(model);
        entity.setProduct(productRepository.findById(model.getProductId()).get());
        entity.setOption1(optionRepository.findById(model.getOptionId1()).get());
        entity.setValue1(valueRepository.findById(model.getValueId1()).get());
        entity.setOption2(optionRepository.findById(model.getOptionId2()).get());
        entity.setValue2(valueRepository.findById(model.getValueId2()).get());
        SKUEntity savedSKU = iskuRepository.save(entity);
        return skuMapper.convertToDto(savedSKU);
    }

    @Override
    public List<SKUDto> addListSKU(long productId, List<ProductOptionDto> optionList) throws IOException {
        try {
            List<SKUDto> stockList = new ArrayList<>();
            if (optionList.size() == 2) {
                List<OptionValueTemp> temps1 = new ArrayList<>();
                List<OptionValueTemp> temps2 = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        for (ProductOptionValueDto value : optionList.get(i).getValues()) {
                            temps1.add(new OptionValueTemp(optionList.get(i), value));
                        }
                    } else {
                        for (ProductOptionValueDto value : optionList.get(i).getValues()) {
                            temps2.add(new OptionValueTemp(optionList.get(i), value));
                        }
                    }
                }
                for (int i = 0; i < temps1.size(); i++) {
                    for (int j = 0; j < temps2.size(); j++) {
                        SKUModel skuModel = new SKUModel();
                        skuModel.setProductId(productId);
                        skuModel.setOptionId1(temps1.get(i).getOption().getId());
                        skuModel.setValueId1(temps1.get(i).getValue().getId());
                        skuModel.setOptionId2(temps2.get(j).getOption().getId());
                        skuModel.setValueId2(temps2.get(j).getValue().getId());
                        stockList.add(saveCustom(skuModel));
                    }
                }
            } else if (optionList.size() == 1) {
                List<OptionValueTemp> temps = new ArrayList<>();
                for (ProductOptionValueDto value : optionList.get(0).getValues()) {
                    temps.add(new OptionValueTemp(optionList.get(0), value));
                }
                for (OptionValueTemp temp : temps) {
                    SKUModel skuModel = new SKUModel();
                    skuModel.setOptionId1(temp.getOption().getId());
                    skuModel.setValueId1(temp.getValue().getId());
                    skuModel.setProductId(productId);
                    stockList.add(saveCustom(skuModel));
                }
            }
            return stockList;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int updateListStock(List<SKUModel> skuModels) throws IOException {
        try {
            for (SKUModel model : skuModels) {
                if (iskuRepository.findById(model.getId()).isPresent()) {
                    SKUEntity entity = iskuRepository.findById(model.getId()).get();
                    entity.setQuantity(model.getQuantity());
                    entity.setPrice(model.getPrice());
                    entity.setImage(model.getImage());
                    iskuRepository.save(entity);
                }
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }
}
