package org.example.final_project.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.validation.PageableValidation;
import org.example.final_project.service.impl.ProductService;
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
@MultipartConfig
@RequestMapping(Const.API_PREFIX + "/product")
@Tag(name = "Product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/")
    ResponseEntity<ApiResponse<?>> getAllByPage(@RequestParam(required = false) Integer pageSize,
                                                @RequestParam(required = false) Integer pageIndex) {
        try {
            Pageable pageable = Pageable.unpaged();
            if (pageSize != null && pageIndex != null) {
                if (pageSize > 0 && pageIndex >= 0) {
                    pageable = PageRequest.of(pageIndex, pageSize);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                            HttpStatus.BAD_REQUEST,
                            "Size Or Index Illegal",
                            null
                    ));
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    productService.findAllByPage(pageable)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PostMapping("/create-new")
    ResponseEntity<ApiResponse<?>> addNewProduct(@ModelAttribute ProductModel model) {
        try {
            productService.save(model);
            return ResponseEntity.ok(createResponse(
                    HttpStatus.CREATED,
                    "Add Product Successfully",
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
    ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable("id") long id,
                                                 @RequestBody ProductModel model) {
        try {
            productService.update(id, model);
            return ResponseEntity.ok(createResponse(HttpStatus.OK,
                    "Update Product Successfully",
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
    ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable("id") long id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Delete Product Successfully",
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

    @PutMapping("/activate/{product-id}")
    ResponseEntity<ApiResponse<?>> inactivateProduct(@PathVariable("product-id") long id,
                                                     @RequestParam int type,
                                                     @RequestParam String note) {
        try {
            productService.inActivateProduct(id, type, note);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.NO_CONTENT,
                    "Inactivate Product Successfully",
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

    @GetMapping("/name/{name}")
    ResponseEntity<ApiResponse<?>> findProductByName(@PathVariable("name") String name,
                                                     @RequestParam(required = false) Integer pageSize,
                                                     @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(
                        200,
                        "Successfully",
                        productService.findAllByNameAndPage(name, PageRequest.of(pageIndex, pageSize)),
                        LocalDateTime.now()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/variable/{product-id}")
    ResponseEntity<ApiResponse<?>> findByParentId(@PathVariable("product-id") long parentId,
                                                  @RequestParam(required = false) Integer pageSize,
                                                  @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        productService.getAllByParentId(parentId, PageableValidation.setDefault(pageSize, pageIndex))
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/status/{type}")
    ResponseEntity<ApiResponse<?>> getAllProductByStatus(@PathVariable("type") int type,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        productService.getAllProductByStatus(type, PageableValidation.setDefault(pageSize, pageIndex))
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
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

    @GetMapping("/relative/{product-id}")
    ResponseEntity<ApiResponse<?>> getAllProductRelative(@PathVariable("product-id") long id,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        productService.getAllProductRelative(id, PageableValidation.setDefault(pageSize, pageIndex))
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/other/{shop-id}")
    ResponseEntity<ApiResponse<?>> getOtherProductOfShop(@PathVariable("shop-id") long productId,
                                                         @RequestParam(required = false) Integer pageSize,
                                                         @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        productService.getOtherProductOfShop(productId, PageableValidation.setDefault(pageSize, pageIndex))
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/shop/{shop-id}")
    ResponseEntity<ApiResponse<?>> getAllProductByShop(@PathVariable("shop-id") long userId,
                                                       @RequestParam(required = false) Integer pageSize,
                                                       @RequestParam(required = false) Integer pageIndex) {
        try {
            if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                        HttpStatus.OK,
                        "Successfully",
                        productService.getAllProductOfShop(userId, PageableValidation.setDefault(pageSize, pageIndex))
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Size Or Index Illegal",
                        null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }
}
