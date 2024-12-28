package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.SKUEntity;
import org.example.final_project.mapper.CartItemMapper;
import org.example.final_project.model.CartItemModel;
import org.example.final_project.repository.ICartItemRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.repository.ISKURepository;
import org.example.final_project.service.ICartItemService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.final_project.specification.CartItemSpecification.*;
import static org.example.final_project.specification.ProductSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemService implements ICartItemService {
    ICartItemRepository cartItemRepository;
    ISKURepository skuRepository;
    CartItemMapper cartItemMapper;
    IProductRepository productRepository;

    @Override
    public int updateQuantity(Long cartId, Long productId, Integer quantity, boolean isAddingOne) {
        if (!isProductValid(productId)) {
            throw new IllegalArgumentException("Product is invalid");
        }

        CartItemEntity currentCartItem = cartItemRepository.findOne(Specification.where(
                hasCartId(cartId).and(hasProductId(productId))
        )).orElse(null);

        SKUEntity product = skuRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product not found")
        );

        if (currentCartItem != null) {
            int newQuantity = isAddingOne
                    ? currentCartItem.getQuantity() + quantity
                    : quantity;

            if (newQuantity > product.getQuantity()) {
                throw new IndexOutOfBoundsException("Requested quantity exceeds available stock");
            }

            currentCartItem.setQuantity(newQuantity);
            currentCartItem.setLastUpdated(LocalDateTime.now());
            cartItemRepository.save(currentCartItem);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteCartItems(Long cartId, List<Long> productIds) {
        Specification<CartItemEntity> specification = Specification.where(hasCartId(cartId));
        List<CartItemEntity> cartItems;
        if (productIds != null && !productIds.isEmpty()) {
            specification = specification.and(hasProductIds(productIds));
        }
        cartItems = cartItemRepository.findAll(specification);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            return cartItems.size();
        }
        return 0;
    }

    @Override
    public List<CartItemDto> getAll() {
        return List.of();
    }

    @Override
    public CartItemDto getById(Long id) {
        return null;
    }

    @Override
    public int save(CartItemModel cartItemModel) {
        if (!isProductValid(cartItemModel.getSkuId())) {
            throw new IllegalArgumentException("Product is invalid");
        }

        Optional<CartItemEntity> currentCartItem = cartItemRepository.findOne(hasCartId(cartItemModel.getCartId()).and(hasProductId(cartItemModel.getSkuId())));
        if (currentCartItem.isPresent()) {
            CartItemEntity cartItem = currentCartItem.get();
            if (cartItemModel.getQuantity() + cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                throw new IndexOutOfBoundsException("Requested quantity exceeds available stock");
            }
            cartItem.setQuantity(cartItem.getQuantity() + cartItemModel.getQuantity());
            cartItem.setLastUpdated(LocalDateTime.now());
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.save(cartItemMapper.toEntity(cartItemModel));
        }
        return 1;
    }

    @Override
    public int update(Long aLong, CartItemModel cartItemModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    private boolean isProductValid(Long id) {
        SKUEntity sku = skuRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Variant not found")
        );
        Specification<ProductEntity> spec = Specification.where(hasId(sku.getProduct().getId()).and(isValid()));
        return productRepository.findOne(spec).isPresent();
    }
}
