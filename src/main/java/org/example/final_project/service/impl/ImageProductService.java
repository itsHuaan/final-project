package org.example.final_project.service.impl;

import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.entity.ImageProduct;
import org.example.final_project.mapper.ImageProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.IImageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageProductService implements IImageProductService {
    @Autowired
    ImageProductMapper imageMapper;
    @Autowired
    IImageProductRepository imageRepository;
    @Override
    public List<ImageProductDto> getAll() {
        return imageRepository.findAll().stream().map(x->imageMapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public ImageProductDto getById(Long id) {
        if(imageRepository.findById(id).get()!=null){
            return imageMapper.convertToDto(imageRepository.findById(id).get());
        }else{
            return null;
        }
    }

    @Override
    public int save(ImageProductModel model) {
        try{
            imageRepository.save(imageMapper.convertToEntity(model));
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int update(Long aLong, ImageProductModel model) {
        try{
            if(imageRepository.findById(aLong).get()!=null){
                ImageProduct imageProduct=imageMapper.convertToEntity(model);
                imageProduct.setId(aLong);

            }
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        try{
            if(imageRepository.findById(id).get()!=null){
                imageRepository.deleteById(id);
            }
            return 1;
        }catch(Exception e){
            System.out.println(e);
            return 0;
        }
    }
}
