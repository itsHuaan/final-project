package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.final_project.dto.CategoryDto;
import org.example.final_project.model.CategoryModel;
import org.example.final_project.service.impl.CategoryService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/category")
@Tag(name = "Category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @GetMapping
    ResponseEntity<?> getAllCategory() {
        List<CategoryDto> categoryDtoList = categoryService.getAll();
        return !categoryDtoList.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "All categories fetched",
                categoryDtoList))
                : ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.NO_CONTENT,
                "No category found",
                null));
    }

    @Operation(summary = "Create new category")
    @PostMapping
    ResponseEntity<?> addNewCategory(@ModelAttribute CategoryModel model) {
        try {
            categoryService.save(model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.CREATED,
                    "Category is added successfully",
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

    @Operation(summary = "Edit a category")
    @PutMapping("/{id}")
    ResponseEntity<?> updateCategory(@PathVariable("id") long id,
                                     @ModelAttribute CategoryModel model) {
        try {
            categoryService.update(id, model);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Category Updated Successfully",
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

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable("id") long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Category is deleted",
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

    @Operation(summary = "Change category status")
    @PutMapping("/activate/{id}")
    ResponseEntity<?> inactivateCategory(@PathVariable("id") long id,
                                         @RequestParam int type) {
        try {
            categoryService.activateCategory(id, type);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Category deactivated successfully",
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

    @Operation(summary = "Get all sub-categories")
    @GetMapping("/{id}/child")
    ResponseEntity<?> findCategoryByParentId(@PathVariable("id") long parentId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "All sub-categories of " + parentId + " fetched",
                    categoryService.getAllByParentId(parentId)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }
}
