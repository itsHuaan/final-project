package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductOptionDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.validation.PageableValidation;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@MultipartConfig
@RequestMapping(Const.API_PREFIX + "/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Product")
public class ProductController {
    @Autowired
    IProductService productService;
    @Autowired
    IProductOptionService optionService;
    @Autowired
    ISKUService iskuService;


    @Operation(summary = "Get product by id")
    @GetMapping("/{product-id}")
    public ResponseEntity<?> getProductById(@PathVariable("product-id") Long productId) {
        ProductDto result = productService.getById(productId);
        return result != null
                ? ResponseEntity.status(HttpStatus.OK).body(
                createResponse(
                        HttpStatus.OK,
                        "Fetched",
                        result
                )
        )
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                createResponse(
                        HttpStatus.OK,
                        "No product found",
                        null
                )
        );
    }

    @Operation(summary = "Get all product")
    @GetMapping
    ResponseEntity<?> getAllByPage(@RequestParam(required = false) Integer pageSize,
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

    @Operation(summary = "Create new product")
    @PostMapping
    ResponseEntity<?> addNewProduct(ProductModel model) {
        try {
            int productId = productService.saveCustom(model);
            List<ProductOptionDto> optionList = optionService.saveAllOption(model.getOptions());
            List<SKUDto> stockList = iskuService.addListSKU(productId, optionList);
            return ResponseEntity.ok(createResponse(
                    HttpStatus.CREATED,
                    "Add Product Successfully",
                    stockList
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@PathVariable("id") long id,
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

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
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

    @Operation(summary = "Change the product status")
    @PutMapping("/activate/{product-id}")
    ResponseEntity<?> inactivateProduct(@PathVariable("product-id") long id,
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

    @Operation(summary = "Search product by its name")
    @GetMapping("/name/{name}")
    ResponseEntity<?> findProductByName(@PathVariable("name") String name,
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

    @Operation(summary = "Get product by its status")
    @GetMapping("/status/{type}")
    ResponseEntity<?> getAllProductByStatus(@PathVariable("type") int type,
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

    @Operation(summary = "Get relative products")
    @GetMapping("/relative/{product-id}")
    ResponseEntity<?> getAllProductRelative(@PathVariable("product-id") long id,
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

    @Operation(summary = "Get shop's other product")
    @GetMapping("/other/{shop-id}")
    ResponseEntity<?> getOtherProductOfShop(@PathVariable("shop-id") long productId,
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

    @Operation(summary = "Get all product by shop")
    @GetMapping("/shop/{shop-id}")
    ResponseEntity<?> getAllProductByShop(@PathVariable("shop-id") long userId,
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
