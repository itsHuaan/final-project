package org.example.final_project.controller;

import org.example.final_project.model.ApiResponse;
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

@RestController
@RequestMapping(Const.API_PREFIX + "/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/getAll")
    ResponseEntity getAllCategory(@RequestParam(required = false) Integer pageSize,
                                  @RequestParam(required = false) Integer pageIndex) {
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        categoryService.findAllByPage(PageRequest.of(pageIndex, pageSize)),
                        LocalDateTime.now()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                        400,
                        "Bad request",
                        null,
                        LocalDateTime.now()
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    200,
                    "Successfully",
                    categoryService.findAllByPage(Pageable.unpaged()),
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/addNew")
    ResponseEntity addNewCategory(@RequestBody CategoryModel model) {
        if (categoryService.save(model) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    201,
                    "Add Category Successfully",
                    null,
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Bad Request",
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/update/{id}")
    ResponseEntity updateCategory(@PathVariable("id") long id,
                                  @RequestBody CategoryModel model) {
        if (categoryService.update(id, model) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    201,
                    "Update Category Successfully",
                    null,
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When Updating Category with Id= " + id,
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/delete/{id}")
    ResponseEntity deleteCategory(@PathVariable("id") long id) {
        if (categoryService.delete(id) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    204,
                    "Delete Category Successfully",
                    null,
                    LocalDateTime.now()
                    ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When Deleting Category with Id= " + id,
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/inactivate/{id}")
    ResponseEntity inactivateCategory(@PathVariable("id") long id) {
        if (categoryService.inActivateCategory(id) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    204,
                    "Inactivate Category Successfully",
                    null,
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When inactivating Category with Id= " + id,
                    null,
                    LocalDateTime.now()
            ));
        }
    }
}
