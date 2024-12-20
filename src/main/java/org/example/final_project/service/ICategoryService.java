package org.example.final_project.service;

import org.example.final_project.dto.CategoryDto;
import org.example.final_project.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService extends IBaseService<CategoryDto, CategoryModel, Long> {
    int activateCategory(long id, int type);

    List<CategoryDto> getAllByParentId(long parent_id);

    Page<CategoryDto> getAllByName(String name, Long parentId, Pageable pageable);

}
