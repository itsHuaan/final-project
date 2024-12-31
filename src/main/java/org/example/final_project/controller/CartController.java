package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.CartDto;
import org.example.final_project.model.AddToCartRequest;
import org.example.final_project.model.CartItemModel;
import org.example.final_project.service.IOrderService;
import org.example.final_project.service.impl.CartItemService;
import org.example.final_project.service.impl.CartService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@RestController
@RequestMapping(Const.API_PREFIX + "/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Cart")
public class CartController {
    CartService cartService;
    CartItemService cartItemService;
    IOrderService orderService;


    @Operation(summary = "Get cart by userId")
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            CartDto cart = cartService.getUserCart(userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    createResponse(HttpStatus.OK,
                            "Fetched cart successfully",
                            cart)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    createResponse(HttpStatus.NOT_FOUND,
                            e.getMessage(),
                            null)
            );
        }
    }

    @Operation(summary = "Get cart quantity")
    @GetMapping("/{userId}/quantity")
    public ResponseEntity<?> getCartQuantity(@PathVariable Long userId) {
        try {
            CartDto cart = cartService.getUserCart(userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    createResponse(HttpStatus.OK,
                            "Cart found",
                            cart.getCartQuantity())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null));
        }
    }

    @Operation(summary = "Remove single or multiple items or clear all from cart",
            description = "Add product IDs to the list to remove them. To remove a single item, add a single product ID to the list. Leave the list blank or add all product IDs to clear the cart.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long userId, @RequestBody List<Long> productIds) {
        try {
            CartDto cart = cartService.getUserCart(userId);
            int result = cartItemService.deleteCartItems(cart.getCartId(), productIds);
            return result != 0
                    ? ResponseEntity.status(HttpStatus.OK).body(
                    createResponse(HttpStatus.OK,
                            "Removed " + result + " products from cart successfully.",
                            null)
            )
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(HttpStatus.BAD_REQUEST,
                            "Failed to remove product.",
                            null)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    createResponse(HttpStatus.NOT_FOUND,
                            e.getMessage(),
                            null)
            );
        }
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateCart(@PathVariable Long userId, @RequestBody AddToCartRequest request) {
        try {
            String message;
            CartDto cart = cartService.getUserCart(userId);
            if (request.getQuantity() > 0) {
                int result = cartItemService.updateQuantity(cart.getCartId(), request.getProductId(), request.getQuantity(), false);
                message = result != 0
                        ? "Quantity for " + request.getProductId() + " updated successfully"
                        : "Failed to update quantity for " + request.getProductId() + ".";
            } else {
                List<Long> productIds = new ArrayList<>();
                productIds.add(request.getProductId());
                int result = cartItemService.deleteCartItems(cart.getCartId(), productIds);
                message = result != 0
                        ? "Removed product " + request.getProductId() + " successfully."
                        : "Failed to remove product " + request.getProductId() + ".";
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    createResponse(HttpStatus.OK,
                            message,
                            null)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    createResponse(HttpStatus.NOT_FOUND,
                            e.getMessage(),
                            null)
            );
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(HttpStatus.BAD_REQUEST,
                            e.getMessage(),
                            null)
            );
        }
    }

    @Operation(summary = "Add to cart")
    @PostMapping("/{userId}")
    public ResponseEntity<?> addToCart(@PathVariable Long userId,
                                       @RequestBody AddToCartRequest request) {
        if (request.getQuantity() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(HttpStatus.BAD_REQUEST,
                            "Invalid quantity.",
                            null)
            );
        }
        try {
            CartDto cart = cartService.getUserCart(userId);
            CartItemModel cartItem = new CartItemModel(cart.getCartId(), request.getProductId(), request.getQuantity());
            int addToCart = cartItemService.save(cartItem);
            if (addToCart == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        createResponse(HttpStatus.CREATED,
                                "Added " + request.getQuantity() + " product of " + request.getProductId() + " to cart.",
                                null)
                );
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        createResponse(HttpStatus.CREATED,
                                "Failed to add product " + request.getProductId() + " to cart.",
                                null)
                );
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    createResponse(HttpStatus.NOT_FOUND,
                            e.getMessage(),
                            null)
            );
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    createResponse(HttpStatus.BAD_REQUEST,
                            e.getMessage(),
                            null)
            );
        }
    }

    @Operation(summary = "Checkout")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkout/{cartId}")
    public ResponseEntity<?> checkout(@PathVariable Long cartId, @RequestParam(required = false) List<Long> cartItemId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCheckOutDetail(cartId, cartItemId));
    }

    @Operation(summary = "Check Quatity")
    @GetMapping("/check-quantity")
    public ResponseEntity<?> checkQuatity(@RequestParam long skuId, @RequestParam long currentQuatity) {
        try {
            ApiResponse<?> response = orderService.checkQuatityInStock(skuId, currentQuatity);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orderService.checkQuatityInStock(skuId, currentQuatity));
        }

    }

    @Operation(summary = "Check Shop Locked")
    @GetMapping("/check-shop-locked")
    public ResponseEntity<?> checkShopLocked(@RequestParam long shopId) {
        try {
            ApiResponse<?> response = cartService.checkShopStatusWhenCheckOut(shopId);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cartService.checkShopStatusWhenCheckOut(shopId));
        }

    }


}
