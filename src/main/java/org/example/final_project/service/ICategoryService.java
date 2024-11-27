package org.example.final_project.service;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService extends IBaseService<CategoryDto, CategoryModel,Long> {
    int inActivateCategory(long id);
    List<CategoryDto> findAllByPage(Pageable pageable);
}
