package org.example.final_project.mapper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.final_project.dto.ImageProductDto;
import org.example.final_project.entity.ImageProductEntity;
import org.example.final_project.model.ImageProductModel;
import org.example.final_project.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ImageProductMapper {

    @Autowired
    Cloudinary cloudinary;
    public ImageProductDto convertToDto(ImageProductEntity image){
        return ImageProductDto.builder()
                .id(image.getId())
                .imageLink(image.getImageLink())
                .build();
    }
    public ImageProductEntity convertToEntity(ImageProductModel model) throws IOException {
        return ImageProductEntity.builder()
                .imageLink(cloudinary.uploader().upload(model.getMultipartFile().getBytes(), ObjectUtils.emptyMap()).get("url").toString())
                .build();
    }
}
