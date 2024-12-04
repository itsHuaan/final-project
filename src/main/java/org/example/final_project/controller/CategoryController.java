package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.service.impl.CategoryService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/category")
@Tag(name = "Category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/get-all")
    ResponseEntity<ApiResponse<?>> getAllCategory(@RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Integer pageIndex) {
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        categoryService.findAllByPage(PageRequest.of(pageIndex, pageSize))));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    categoryService.findAllByPage(Pageable.unpaged())
            ));
        }
    }

    @PostMapping("/create-new")
    ResponseEntity<ApiResponse<?>> addNewCategory(@ModelAttribute CategoryModel model) {
        try {
            categoryService.save(model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.CREATED,
                    "Add Category Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable("id") long id,
                                                  @ModelAttribute CategoryModel model) {
        try {
            categoryService.update(id, model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Update Category Successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable("id") long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Delete Category Successfully",
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PutMapping("/activate/{id}")
    ResponseEntity<ApiResponse<?>> inactivateCategory(@PathVariable("id") long id,
                                                      @RequestParam int type) {
        try {
            categoryService.activateCategory(id, type);
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.NO_CONTENT,
                        "Inactivate Category Successfully",
                        null
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/{id}/child")
    ResponseEntity<ApiResponse<?>> findCategoryByParentId(@PathVariable("id") long parentId,
                                                          @RequestParam(required = false) Integer pageSize,
                                                          @RequestParam(required = false) Integer pageIndex) {
        try {
            if (pageSize != null && pageIndex != null) {
                if (pageSize > 0 && pageIndex >= 0) {
                    return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                            HttpStatus.OK,
                            "Successfully",
                            categoryService.getAllByParentId(parentId, PageRequest.of(pageIndex, pageSize))
                    ));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                            HttpStatus.BAD_REQUEST,
                            "Size Or Index Illegal",
                            null
                    ));
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        categoryService.getAllByParentId(parentId, Pageable.unpaged())
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }
}
