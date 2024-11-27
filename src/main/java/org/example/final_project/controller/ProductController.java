package org.example.final_project.controller;


import jakarta.servlet.annotation.MultipartConfig;
import org.example.final_project.model.ApiResponse;
import org.example.final_project.model.ProductModel;
import org.example.final_project.service.impl.ProductService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@MultipartConfig
@RequestMapping(Const.API_PREFIX + "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/getAll")
    ResponseEntity<ApiResponse<?>> getAllByPage(@RequestParam(required = false) Integer pageSize,
                                @RequestParam(required = false) Integer pageIndex) {
        Pageable pageable = Pageable.unpaged();
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0) {
                pageable = PageRequest.of(pageIndex, pageSize);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                        400,
                        "Bad Request",
                        null,
                        LocalDateTime.now()
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                200,
                "Successfully",
                productService.findAllByPage(pageable),
                LocalDateTime.now()
        ));
    }

    @PostMapping("/addNew")
    ResponseEntity<ApiResponse<?>> addNewProduct(@ModelAttribute ProductModel model) {
        if (productService.save(model) == 1) {
            return ResponseEntity.ok(new ApiResponse<>(
                    201,
                    "Add Product Successfully",
                    null,
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When Adding New Product",
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/update/{id}")
    ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable("id") long id,
                                 @RequestBody ProductModel model) {
        if (productService.update(id, model) == 1) {
            return ResponseEntity.ok(new ApiResponse<>(204,
                    "Update Product Successfully",
                    null,
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When Updating Product with Id= " + id,
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable("id") long id) {
        if (productService.delete(id) == 1) {
            return ResponseEntity.ok(new ApiResponse<>(
                    204,
                    "Delete Product Successfully",
                    null,
                    LocalDateTime.now()
                    ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When Deleting Product with Id= " + id,
                    null,
                    LocalDateTime.now()
                    ));
        }
    }

    @PostMapping("/inactivate/{id}")
    ResponseEntity<ApiResponse<?>> inactivateProduct(@PathVariable("id") long id) {
        if (productService.inActivateProduct(id) == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    204,
                    "Inactivate Product Successfully",
                    null,
                    LocalDateTime.now()
                    ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Occur Error When inactivating Product with Id= " + id,
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/findByName/{name}")
    ResponseEntity<ApiResponse<?>> findProductByName(@PathVariable("name") String name,
                                     @RequestParam(required = false) Integer pageSize,
                                     @RequestParam(required = false) Integer pageIndex) {
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        productService.findAllByNameAndPage(name, PageRequest.of(pageIndex, pageSize)),
                        LocalDateTime.now()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                        400,
                        "Pageable error",
                        null,
                        LocalDateTime.now()
                        ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    200,
                    "Successfully",
                    productService.findAllByNameAndPage(name, Pageable.unpaged()),
                    LocalDateTime.now())
            );
        }
    }
}
