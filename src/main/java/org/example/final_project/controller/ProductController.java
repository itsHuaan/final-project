package org.example.final_project.controller;


import com.cloudinary.Api;
import jakarta.servlet.annotation.MultipartConfig;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.validation.PageableValidation;
import org.example.final_project.service.impl.ProductService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    @GetMapping("/")
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

    @PostMapping("/create-new")
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

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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

    @PutMapping("/activate/{product-id}")
    ResponseEntity<ApiResponse<?>> inactivateProduct(@PathVariable("product-id") long id,
                                                     @RequestParam int type,
                                                     @RequestParam String note) {
        try {
            if (productService.inActivateProduct(id, type, note) == 1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        HttpStatus.NO_CONTENT.value(),
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/name/{name}")
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

    @GetMapping("/variable/{product-id}")
    ResponseEntity<ApiResponse<?>> findByParentId(@PathVariable("product-id") long parentId,
                                                  @RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Integer pageIndex) {
        if (pageSize != null && pageIndex != null) {
            if (pageSize > 0 && pageIndex >= 0) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        productService.getAllByParentId(parentId, PageRequest.of(pageIndex, pageSize)),
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
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    200,
                    "Successfully",
                    productService.getAllByParentId(parentId, Pageable.unpaged()),
                    LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/status/{type}")
    ResponseEntity<ApiResponse<?>> getAllProductByStatus(@PathVariable("type") int type,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        productService.getAllProductByStatus(type, PageableValidation.setDefault(pageSize, pageIndex)),
                        LocalDateTime.now()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                        400,
                        "Check page size and page index",
                        null,
                        LocalDateTime.now()
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, e.getMessage(), null, LocalDateTime.now()));
        }
    }

    @GetMapping("/relative/{product-id}")
    ResponseEntity<ApiResponse<?>> getAllProductRelative(@PathVariable("product-id") long id,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        productService.getAllProductRelative(id, PageableValidation.setDefault(pageSize, pageIndex)),
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Value Not Found",
                    null,
                    LocalDateTime.now()
            ));
        }
    }

    @GetMapping("/other/{shop-id}")
    ResponseEntity<ApiResponse<?>> getOtherProductOfShop(@PathVariable("shop-id") long productId,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try{
        if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    200,
                    "Successfully",
                    productService.getOtherProductOfShop(productId, PageableValidation.setDefault(pageSize, pageIndex)),
                    LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Bad Request",
                    null,
                    LocalDateTime.now()
            ));
        }}catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Value Not Found",
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    @GetMapping("/shop/{shop-id}")
    ResponseEntity<ApiResponse<?>> getAllProductByShop(@PathVariable("shop-id")long userId,
                                                       @RequestParam(required = false)Integer pageSize,
                                                       @RequestParam(required = false)Integer pageIndex){
        try{
        if(PageableValidation.setDefault(pageSize,pageIndex)!=null){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                    200,
                    "Successfully",
                    productService.getAllProductOfShop(userId, PageableValidation.setDefault(pageSize, pageIndex)),
                    LocalDateTime.now()
            ));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    "Invalid page size or page index",
                    null,
                    LocalDateTime.now()
            ));
        }}catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
}
