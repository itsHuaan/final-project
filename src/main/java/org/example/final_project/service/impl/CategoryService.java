package org.example.final_project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.CategoryDto;
import org.example.final_project.entity.CategoryEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.CategoryMapper;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.model.enum_status.ActivateStatus;
import org.example.final_project.repository.ICategoryRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.specification.CategorySpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    ICategoryRepository iCategoryRepository;
    CategoryMapper categoryMapper;
    IUserRepository iUserRepository;
    Cloudinary cloudinary;

    @Override
    public List<CategoryDto> getAll() {
        return iCategoryRepository.findAll(Specification.where(isNotDeleted())).stream()
                .map(categoryMapper::convertToDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return iCategoryRepository.findById(id).isPresent()
                ? categoryMapper.convertToDto(iCategoryRepository.findById(id).get())
                : null;
    }

    @Override
    public int save(CategoryModel model) {
        try {
            CategoryEntity category = categoryMapper.convertToEntity(model);
            if (model.getFile() != null && !model.getFile().isEmpty()) {
                category.setImage(cloudinary.uploader().upload(model.getFile().getBytes(), ObjectUtils.emptyMap()).get("url").toString());
            }
            if (model.getUser_id() != 0L) {
                UserEntity user = iUserRepository.findById(model.getUser_id()).isPresent()
                        ? iUserRepository.findById(model.getUser_id()).get()
                        : new UserEntity();
                category.setUser(user);
            }
            iCategoryRepository.save(category);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public int update(Long aLong, CategoryModel model) {
        try {
            CategoryEntity category = categoryMapper.convertToEntity(model);
            if (iCategoryRepository.findById(aLong).isPresent()) {
                if (model.getFile() != null && !model.getFile().isEmpty()) {
                    category.setImage(cloudinary.uploader().upload(model.getFile().getBytes(), ObjectUtils.emptyMap()).get("url").toString());
                }
                if (model.getParent_id() != 0L) {
                    if (iCategoryRepository.findById(model.getParent_id()).isPresent()) {
                        model.setParent_id(model.getParent_id());
                    } else {
                        throw new IllegalArgumentException("Parent Category is not present");
                    }
                }
                iCategoryRepository.save(category);
            } else {
                throw new IllegalArgumentException("Category is not present");
            }
            return 1;
        } catch (
                Exception e) {
            log.error(e.getMessage());
            return 0;
        }

    }

    @Override
    public int delete(Long id) {
        try {
            if (iCategoryRepository.findById(id).isPresent()) {
                CategoryEntity categoryEntity = iCategoryRepository.findById(id).get();
                categoryEntity.setDeletedAt(LocalDateTime.now());
                iCategoryRepository.save(categoryEntity);
            } else {
                throw new IllegalArgumentException("Value Not Found");
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }

    }

    @Override
    public int activateCategory(long id, int type) {
        try {
            if (iCategoryRepository.findById(id).isPresent()) {
                CategoryEntity categoryEntity = iCategoryRepository.findById(id).get();
                if (ActivateStatus.Inactive.checkIfExist(type)) {
                    categoryEntity.setIsActive(type);
                    iCategoryRepository.save(categoryEntity);
                } else {
                    throw new IllegalArgumentException("Value Not Found");
                }
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<CategoryDto> getAllByParentId(long parent_id) {
        return iCategoryRepository.findAll(Specification.where(isNotDeleted()).and(hasParentId(parent_id))).stream()
                .map(categoryMapper::convertToDto)
                .toList();
    }

    @Override
    public Page<CategoryDto> getAllByName(String name, Long parentId, Pageable pageable) {
        Specification<CategoryEntity> specification = Specification.where(isNotDeleted());
        if (parentId != 0L) {
            if (iCategoryRepository.findById(parentId).isPresent()) {
                specification = specification.and(hasParentId(parentId));
                if (name != null) {
                    specification = specification.and(hasName(name));
                }
            } else {
                throw new IllegalArgumentException("Parent is not present");
            }
        } else {
            specification = specification.and(hasParentId(parentId));
            if (name != null) {
                specification = specification.and(hasName(name));
            }
        }
        return iCategoryRepository.findAll(specification, pageable).map(x -> categoryMapper.convertToDto(x));
    }
}
