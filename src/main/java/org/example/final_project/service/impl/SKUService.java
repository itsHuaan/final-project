package org.example.final_project.service.impl;

import com.cloudinary.Cloudinary;
import org.example.final_project.dto.*;
import org.example.final_project.entity.ProductOptionValuesEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.mapper.SKUMapper;
import org.example.final_project.model.SKUModel;
import org.example.final_project.repository.IProductOptionRepository;
import org.example.final_project.repository.IProductOptionValueRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductOptionValueService;
import org.example.final_project.service.ISKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
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
    IProductOptionService optionService;
    @Autowired
    IProductOptionValueService valueService;
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
        entity.setProduct(productRepository.findById(model.getProductId()).orElseThrow(
                () -> new NoSuchElementException("Value invalid")
        ));
        entity.setOption1(optionRepository.findById(model.getOptionId1()).orElseThrow(
                () -> new NoSuchElementException("Value invalid")
        ));
        entity.setValue1(valueRepository.findById(model.getValueId1()).orElseThrow(
                () -> new NoSuchElementException("Value invalid")
        ));
        if (model.getOptionId2() != null && model.getValueId2() != null) {
            entity.setOption2(optionRepository.findById(model.getOptionId2()).orElseThrow(
                    () -> new NoSuchElementException("Value invalid")
            ));
            entity.setValue2(valueRepository.findById(model.getValueId2()).orElseThrow(
                    () -> new NoSuchElementException("Value invalid")
            ));
        }
        SKUEntity savedSKU = iskuRepository.save(entity);
        return skuMapper.convertToDto(savedSKU);
    }

    @Override
    public List<SKUDto> addListSKU(long productId, List<ProductOptionDetailDto> optionList) throws IOException {
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
                        skuModel.setValueId1(temps1.get(i).getValue().getValueId());
                        skuModel.setOptionId2(temps2.get(j).getOption().getId());
                        skuModel.setValueId2(temps2.get(j).getValue().getValueId());
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
                    skuModel.setValueId1(temp.getValue().getValueId());
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
                    if (model.getImage() != null) {
                        entity.setImage(model.getImage());
                    }
                    iskuRepository.save(entity);
                }
            }
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Set<Long> getAllOptionOfProduct(long productId) {
        if (productRepository.findById(productId).isPresent()) {
            Set<Long> optionIdList = new HashSet<>();
            List<SKUEntity> skuList = iskuRepository.findAllByProduct_Id(productId);
            for (SKUEntity sku : skuList) {
                optionIdList.add(sku.getOption1().getId());
                if (sku.getOption2() != null) {
                    optionIdList.add(sku.getOption2().getId());
                }
            }
            return optionIdList;
        } else {
            return null;
        }
    }

    @Override
    public int addListSKU(Long productId, Long optionId) {
        try {
            if (productRepository.findById(productId).isPresent()) {
                List<SKUDto> skuList = getAllByProduct(productId);
                Set<Long> optionIdList = new HashSet<>();
                for (SKUDto skuDto : skuList) {
                    optionIdList.add(skuDto.getOption1().getOptionId());
                }
                deleteAllSKUByOptionId(optionIdList.stream().toList().get(0));
                // lấy ra tất cả các option cũ của sản phẩm
                if (optionId != null) {
                    for (Long oldOptionId : optionIdList) {
                        if (oldOptionId != optionId) {
                            List<OptionValueTemp> temp1 = new ArrayList<>();//list option-value mới
                            for (ProductOptionValuesEntity value : valueRepository.findAllByOption_Id(optionId)) {
                                temp1.add(new OptionValueTemp(optionService.getById(optionId), valueService.getById(value.getId())));
                            }
                            List<OptionValueTemp> temp2 = new ArrayList<>();//list option-value cũ
                            for (ProductOptionValuesEntity value : valueRepository.findAllByOption_Id(oldOptionId)) {
                                temp2.add(new OptionValueTemp(optionService.getById(oldOptionId), valueService.getById(value.getId())));
                            }
                            for (OptionValueTemp newTemp : temp1) {
                                for (OptionValueTemp oldTemp : temp2) {
                                    SKUModel skuModel = new SKUModel();
                                    skuModel.setProductId(productId);
                                    skuModel.setOptionId1(oldTemp.getOption().getId());
                                    skuModel.setValueId1(oldTemp.getValue().getValueId());
                                    skuModel.setOptionId2(newTemp.getOption().getId());
                                    skuModel.setValueId2(newTemp.getValue().getValueId());
                                    saveCustom(skuModel);
                                }
                            }
                        }
                    }
                }
                return 1;
            } else {
                throw new IndexOutOfBoundsException("Out of options size");
            }
        } catch (
                Exception e) {
            throw e;
        }
    }

    @Override
    public int addListSKUAfterDeleteOption(Long productId, Long optionId) {
        try {
            if (productRepository.findById(productId).isPresent()) {
                List<SKUDto> skuList = getAllByProduct(productId);
                Set<Long> optionIdList = new HashSet<>();
                for (SKUDto skuDto : skuList) {
                    if (skuDto.getOption1() != null) {
                        optionIdList.add(skuDto.getOption1().getOptionId());
                    }
                    if (skuDto.getOption2() != null) {
                        optionIdList.add(skuDto.getOption2().getOptionId());
                    }
                }
                optionService.delete(optionId);
                for (Long id : optionIdList) {
                    if (id != optionId) {
                        for (ProductOptionValuesEntity value : valueRepository.findAllByOption_Id(id)) {
                            SKUModel skuModel = new SKUModel();
                            skuModel.setOptionId1(id);
                            skuModel.setValueId1(value.getId());
                            skuModel.setProductId(productId);
                            saveCustom(skuModel);
                        }
                    }
                }
                return 1;
            } else {
                throw new IndexOutOfBoundsException("Out of options size");
            }
        } catch (
                Exception e) {
            throw e;
        }
    }


    public int deleteAllSKUByOptionId(Long optionId) {
        try {
            List<SKUEntity> skuEntities = iskuRepository.findAllByOption1_IdOrOption2_Id(optionId, optionId);
            iskuRepository.deleteAll(skuEntities);
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }
}
