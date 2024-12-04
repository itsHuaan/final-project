package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
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


    @Operation(summary = "Add to cart")
    @PostMapping("add-to-cart/{userId}")
    public ResponseEntity<?> addToCart(@PathVariable Long userId,
                                       @RequestBody AddToCartRequest request) {
        try {
            CartDto cart = cartService.getUserCart(userId);
            CartItemDto cartItem = cartItemService.getCartItem(cart.getCartId(), request.getProductId());
            cartItemService.updateQuantity(cartItem.getCartId(), request.getProductId(), request.getQuantity());
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
}
