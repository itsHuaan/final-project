package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ProductDto;
import org.example.final_project.dto.ProductOptionDetailDto;
import org.example.final_project.dto.ProductSummaryDto;
import org.example.final_project.model.FavoriteProductModel;
import org.example.final_project.model.ProductModel;
import org.example.final_project.service.IFavoriteProductService;
import org.example.final_project.service.IProductOptionService;
import org.example.final_project.service.IProductService;
import org.example.final_project.service.ISKUService;
import org.example.final_project.util.Const;
import org.example.final_project.validation.PageableValidation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @Operation(summary = "Get product by id, type = 0: admin, type = 1:user ")
    @GetMapping("/{product-id}")
    public ResponseEntity<?> getProductById(@PathVariable("product-id") Long productId,
                                            @RequestParam Integer type) {
        try {
            ProductDto result = productService.getByIdCustom(productId, type);
            return result != null
                    ? ResponseEntity.status(HttpStatus.OK).body(
                    createResponse(
                            HttpStatus.OK,
                            "Product found",
                            result
                    )
            )
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(
                            HttpStatus.BAD_REQUEST,
                            "No product found",
                            null
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(
                            HttpStatus.BAD_REQUEST,
                            e.getMessage(),
                            null
                    )
            );
        }
    }

    @Operation(summary = "Get all product")
    @GetMapping
    ResponseEntity<?> getAllByPage(@RequestParam(required = false) Integer pageSize,
                                   @RequestParam(required = false) Integer pageIndex,
                                   @RequestParam Integer type) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all product successfully",
                    productService.findAllByPage(type, PageableValidation.setDefault(pageSize, pageIndex))
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PreAuthorize("hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create new product")
    @PostMapping
    ResponseEntity<?> addNewProduct(ProductModel model) {
        try {
            int productId = productService.saveCustom(model);
            List<ProductOptionDetailDto> optionList = optionService.saveAllOption(model.getOptions());
            iskuService.addListSKU(productId, optionList);
            return ResponseEntity.ok(createResponse(
                    HttpStatus.CREATED,
                    "Add Product Successfully",
                    productId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PreAuthorize("hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@PathVariable("id") long id,
                                    ProductModel model) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
//                if (userDetails.getUser().getUserId() == model.getUser_id()) {
            productService.update(id, model);
            return ResponseEntity.ok(createResponse(HttpStatus.OK,
                    "Update Product Successfully",
                    null
            ));
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
//                            HttpStatus.BAD_REQUEST,
//                            "Product not in shop",
//                            null
//                    ));
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
//                        HttpStatus.BAD_REQUEST,
//                        "Something went wrong",
//                        null
//                ));
//            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }

    @PreAuthorize("hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
        int result = productService.delete(id);
        return result != 0
                ? ResponseEntity.ok(createResponse(
                HttpStatus.NO_CONTENT,
                "Delete Product Successfully",
                null
        ))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                HttpStatus.BAD_REQUEST,
                "Product not found",
                null
        ));
    }


    @PreAuthorize("hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
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
                                        @RequestParam Integer type,
                                        @RequestParam(required = false) Integer pageSize,
                                        @RequestParam(required = false) Integer pageIndex) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Product found",
                    productService.findAllByNameAndPage(type, name, PageableValidation.setDefault(pageSize, pageIndex))
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
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
            Page<ProductSummaryDto> page = productService.getAllProductByStatus(type, PageableValidation.setDefault(pageSize, pageIndex));
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all product successfully",
                    productService.getAllProductByStatus(type, PageableValidation.setDefault(pageSize, pageIndex))
            ));
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
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all product successfully",
                    productService.getAllProductRelative(id, PageableValidation.setDefault(pageSize, pageIndex))
            ));
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
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all product successfully",
                    productService.getOtherProductOfShop(productId, PageableValidation.setDefault(pageSize, pageIndex))
            ));
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
                                          @RequestParam Integer type,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Integer pageIndex) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all product successfully",
                    productService.getAllProductOfShop(userId, type, PageableValidation.setDefault(pageSize, pageIndex))
            ));
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
    ResponseEntity<?> getAllProductByFilter(@RequestParam Integer type,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) List<Long> categoryId,
                                            @RequestParam(required = false) List<Long> addressId,
                                            @RequestParam(required = false) Double startPrice,
                                            @RequestParam(required = false) Double endPrice,
                                            @RequestParam(required = false) Double rating,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestParam(required = false) Integer pageIndex) {
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                HttpStatus.OK,
                "Fetched all product successfully",
                productService.getAllProductByFilter(type, name, categoryId, addressId, startPrice, endPrice, rating, PageableValidation.setDefault(pageSize, pageIndex))
        ));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add to favorite")
    @PostMapping("/favorite")
    ResponseEntity<?> addToFavorite(@RequestBody FavoriteProductModel favoriteProduct) {
        try {
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    "There's an error occurred",
                    null
            ));
        }
    }

    @Operation(summary = "Get All Product In Promotion")
    @GetMapping("/promotion/{promotion-id}")
    ResponseEntity<?> getAllProductByPromotion(@RequestParam Integer type,
                                               @PathVariable("promotion-id") Long promotionId,
                                               @RequestParam(required = false) Long shopId,
                                               @RequestParam(required = false) Integer pageIndex,
                                               @RequestParam(required = false) Integer pageSize) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(
                    HttpStatus.OK,
                    "Fetched all promotion successfully",
                    productService.getAllProductByPromotion(type, promotionId, shopId, PageableValidation.setDefault(pageSize, pageIndex))
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null
            ));
        }
    }
}
