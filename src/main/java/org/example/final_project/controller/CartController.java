package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartDto;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.dto.SKUDto;
import org.example.final_project.model.AddToCartRequest;
import org.example.final_project.service.ISKUService;
import org.example.final_project.service.impl.CartItemService;
import org.example.final_project.service.impl.CartService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ISKUService skuService;

    @Operation(summary = "Get cart by userId")
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
            CartItemDto cartItem = cartItemService.getCartItem(cart.getCartId(), request.getProductId());
            if (request.getQuantity() > 0) {
                int result = cartItemService.updateQuantity(cartItem.getCartId(), request.getProductId(), request.getQuantity(), false);
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
        }
    }

    @Operation(summary = "Add to cart")
    @PostMapping("/{userId}")
    public ResponseEntity<?> addToCart(@PathVariable Long userId,
                                       @RequestBody AddToCartRequest request) {
        try {
            CartDto cart = cartService.getUserCart(userId);
            CartItemDto cartItem = cartItemService.getCartItem(cart.getCartId(), request.getProductId());
            cartItemService.updateQuantity(cartItem.getCartId(), request.getProductId(), request.getQuantity(), true);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    createResponse(HttpStatus.CREATED,
                            "Added " + request.getQuantity() + " product of " + request.getProductId() + " to cart.",
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

    @Operation(summary = "Checkout")
    @GetMapping("/checkout/{cartId}")
    public ResponseEntity<?> checkout(@PathVariable Long cartId, @RequestParam List<Long> productId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCheckOutDetail(cartId, productId));
    }
}
