package org.example.final_project.service.impl;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.mapper.CategoryMapper;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.model.enum_status.ActivateStatus;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.ICategoryService;
import org.example.final_project.util.specification.CategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.final_project.util.specification.CategorySpecification.isActive;
import static org.example.final_project.util.specification.CategorySpecification.isNotDeleted;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository iCategoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    IUserRepository iUserRepository;

    @Override
    public List<CategoryDto> getAll() {
        return iCategoryRepository.findAll().stream().filter(x -> x.getDeletedAt() == null && x.getIsActive() == 1).map(x -> categoryMapper.convertToDto(x)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        if (iCategoryRepository.findById(id).get() != null) {
            return categoryMapper.convertToDto(iCategoryRepository.findById(id).get());
        } else {
            return null;
        }
    }

    @Override
    public int save(CategoryModel model) {
        try {
            CategoryEntity category = categoryMapper.convertToEntity(model);
            if (model.getUser_id() != 0L) {
                category.setUser(iUserRepository.findById(model.getUser_id()).get());
            }
            iCategoryRepository.save(category);
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int update(Long aLong, CategoryModel model) {
        try {
            if (iCategoryRepository.findById(aLong).get() != null) {
                CategoryEntity categoryEntity = categoryMapper.convertToEntity(model);
                categoryEntity.setId(aLong);
                iCategoryRepository.save(categoryEntity);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        try {
            CategoryEntity categoryEntity = iCategoryRepository.findById(id).get();
            if (categoryEntity != null) {
                categoryEntity.setDeletedAt(LocalDateTime.now());
                iCategoryRepository.save(categoryEntity);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }

    }

    @Override
    public int activateCategory(long id, int type) {
        try {
            CategoryEntity categoryEntity = iCategoryRepository.findById(id).get();
            if (categoryEntity != null) {
                if (EnumSet.of(ActivateStatus.Active, ActivateStatus.Inactive).contains(type)) {
                    categoryEntity.setIsActive( type);
                    iCategoryRepository.save(categoryEntity);
                }
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }


    @Override
    public Page<CategoryDto> findAllByPage(Pageable pageable) {
        if (pageable != null) {
            Page<CategoryDto> page = iCategoryRepository.findAll(Specification.where(isActive().and(isNotDeleted())), pageable).map(x -> categoryMapper.convertToDto(x));
            return page;
        } else {
            Page<CategoryDto> page = iCategoryRepository.findAll(Specification.where(isActive().and(isNotDeleted())), PageRequest.of(0, iCategoryRepository.findAll().size())).map(x -> categoryMapper.convertToDto(x));
            return page;
        }
    }
}
