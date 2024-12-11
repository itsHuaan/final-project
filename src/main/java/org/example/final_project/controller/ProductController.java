package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.configuration.UserDetailsImpl;
import org.example.final_project.dto.*;
import org.example.final_project.model.FavoriteProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.model.validation.PageableValidation;
import org.example.final_project.service.IFavoriteProductService;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    IProductService productService;
    IProductOptionService optionService;
    ISKUService iskuService;
    IFavoriteProductService favoriteProductService;


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
            List<ProductOptionDetailDto> optionList = optionService.saveAllOption(model.getOptions());
            List<SKUDto> stockList = iskuService.addListSKU(productId, optionList);
            return ResponseEntity.ok(createResponse(
                    HttpStatus.CREATED,
                    "Add Product Successfully",
                    productId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SELLER')")
    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@PathVariable("id") long id,
                                    ProductModel model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
                if (userDetails.getUser().getUserId() == model.getUser_id()) {
                    productService.update(id, model);
                    return ResponseEntity.ok(createResponse(HttpStatus.OK,
                            "Update Product Successfully",
                            null
                    ));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                            HttpStatus.BAD_REQUEST,
                            "Product not in shop",
                            null
                    ));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong",
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
            productService.deactivateProduct(id, type, note);
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

    @Operation(summary = "Get shop's other product", description = "Get all the products of the shop except for the selected product.")
    @GetMapping("/other/{product-id}")
    ResponseEntity<?> getOtherProductOfShop(@PathVariable("product-id") long productId,
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

    @Operation(summary = "Filter product")
    @GetMapping("/filter")
    ResponseEntity<?> getAllProductByFilter(@RequestParam(required = false) List<Long> categoryId,
                                            @RequestParam(required = false) List<Long> addressId,
                                            @RequestParam(required = false) Double startPrice,
                                            @RequestParam(required = false) Double endPrice,
                                            @RequestParam(required = false) Double rating,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestParam(required = false) Integer pageIndex) {
        if (PageableValidation.setDefault(pageSize, pageIndex) != null) {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Successfully",
                    productService.getAllProductByFilter(categoryId, addressId, startPrice, endPrice, rating, PageableValidation.setDefault(pageSize, pageIndex))
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Page size or index",
                    null
            ));
        }
    }

    @Operation(summary = "Add to favorite")
    @PostMapping("/favorite")
    ResponseEntity<?> addToFavorite(@RequestBody FavoriteProductModel favoriteProduct) {
        int result = favoriteProductService.save(favoriteProduct);
        return result == 1
                ? ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Added to favorite.",
                null
        ))
                : ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Remove from favorite.",
                null
        ));
    }
}
