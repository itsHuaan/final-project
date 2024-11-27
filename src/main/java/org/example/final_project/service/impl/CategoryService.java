package org.example.final_project.service.impl;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.mapper.CategoryMapper;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    ICategoryRepository iCategoryRepository;
    @Autowired
    CategoryMapper categoryMapper;

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
            iCategoryRepository.save(categoryMapper.convertToEntity(model));
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
    public int inActivateCategory(long id) {
        try {
            CategoryEntity categoryEntity = iCategoryRepository.findById(id).get();
            if (categoryEntity != null) {
                categoryEntity.setIsActive(0);
                iCategoryRepository.save(categoryEntity);
            }
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public List<CategoryDto> findAllByPage(Pageable pageable) {
        if (pageable != null) {
            List<CategoryDto> page = iCategoryRepository.findAll(pageable).stream().filter(x->x.getIsActive()==1&&x.getDeletedAt()==null).map(x->categoryMapper.convertToDto(x)).collect(Collectors.toList());
            return page;
        } else {
            List<CategoryDto> page= iCategoryRepository.findAll(PageRequest.of(0,iCategoryRepository.findAll().size())).stream().filter(x->x.getIsActive()==1&&x.getDeletedAt()==null).map(x->categoryMapper.convertToDto(x)).collect(Collectors.toList());
            return page;
        }
    }
}
