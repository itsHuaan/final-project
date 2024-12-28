package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.entity.ImageProductEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.mapper.ImageProductMapper;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.repository.IImageProductRepository;
import org.example.final_project.service.IImageProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ImageProductService implements IImageProductService {
    ImageProductMapper imageMapper;
    IImageProductRepository imageRepository;

    @Override
    public List<ImageProductDto> getAll() {
        return imageRepository.findAll().stream().map(imageMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ImageProductDto getById(Long id) {
        if (imageRepository.findById(id).isPresent()) {
            return imageMapper.convertToDto(imageRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public int save(ImageProductModel model) {
        try {
            ImageProductEntity imageProduct = imageMapper.convertToEntity(model);
            imageProduct.setProductEntity(ProductEntity.builder().id(model.getProductId()).build());
            imageRepository.save(imageProduct);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Long aLong, ImageProductModel model) {
        try {
            if (imageRepository.findById(aLong).isPresent()) {
                ImageProductEntity imageProductEntity = imageMapper.convertToEntity(model);
                imageProductEntity.setId(aLong);
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        try {
            if (imageRepository.findById(id).isPresent()) {
                imageRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("Value not present");
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
}
