package org.example.final_project.service.impl;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.Category;
import org.example.final_project.mapper.CategoryMapper;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        return iCategoryRepository.findAll().stream().filter(x -> x.getDeletedAt() == null && x.isActive()).map(x -> categoryMapper.convertToDto(x)).collect(Collectors.toList());
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
                Category category = categoryMapper.convertToEntity(model);
                category.setId(aLong);
                iCategoryRepository.save(category);
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
            Category category = iCategoryRepository.findById(id).get();
            if (category != null) {
                category.setDeletedAt(LocalDateTime.now());
                iCategoryRepository.save(category);
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
            Category category = iCategoryRepository.findById(id).get();
            if (category != null) {
                category.setActive(false);
                iCategoryRepository.save(category);
            }
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    @Override
    public Page<CategoryDto> findAllByPage(Pageable pageable) {
        return new PageImpl<>(iCategoryRepository.findAll().stream().filter(x->x.isActive()).map(x->categoryMapper.convertToDto(x)).collect(Collectors.toList()),pageable,iCategoryRepository.findAll(pageable).getTotalElements());
    }
}
