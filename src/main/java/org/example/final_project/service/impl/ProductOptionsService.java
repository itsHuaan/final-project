package org.example.final_project.service.impl;
import org.example.final_project.dto.ProductOptionsDto;
import org.example.final_project.entity.ProductOptionsEntity;
import org.example.final_project.mapper.ProductOptionsMapper;
import org.example.final_project.model.ProductOptionsModel;
import org.example.final_project.repository.IProductOptionsRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IProductOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOptionsService implements IProductOptionsService {
    @Autowired
    IProductOptionsRepository optionsRepository;
    @Autowired
    ProductOptionsMapper optionsMapper;
    @Autowired
    IProductRepository productRepository;

    @Override
    public List<ProductOptionsDto> getAll() {
        try {
            return optionsRepository.findAll().stream().map(x -> optionsMapper.convertToDto(x)).collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ProductOptionsDto getById(Long id) {
        try {
            if (optionsRepository.findById(id).isPresent()) {
                return optionsMapper.convertToDto(optionsRepository.findById(id).get());
            }else{
                throw new IllegalArgumentException("Value not found");
            }
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int save(ProductOptionsModel model) {
        try{
            ProductOptionsEntity entity=optionsMapper.convertToEntity(model);
            entity.setProduct(productRepository.findById(model.getProductId()).get());
            optionsRepository.save(entity);
            return 1;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public int update(Long aLong, ProductOptionsModel model) {
        try{
            if(optionsRepository.findById(aLong).isPresent()){
                ProductOptionsEntity entity=optionsMapper.convertToEntity(model);
                entity.setId(aLong);
                optionsRepository.save(entity);
                return 1;
            }else{
                throw new IllegalArgumentException("Value Not Found");
            }
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public int delete(Long id) {
        try{
            if(optionsRepository.findById(id).isPresent()){
                optionsRepository.deleteById(id);
                return 1;
            }else{
                throw new IllegalArgumentException("Value Not Found");
            }
        }catch (Exception e){
            throw e;
        }
    }
}
