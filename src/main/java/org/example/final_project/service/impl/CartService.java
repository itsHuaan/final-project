package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartDto;
import org.example.final_project.dto.CartItemDto;
import org.example.final_project.dto.CheckoutDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.entity.CartItemEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.mapper.CartItemMapper;
import org.example.final_project.mapper.CartMapper;
import org.example.final_project.mapper.UserMapper;
import org.example.final_project.model.CartModel;
import org.example.final_project.repository.ICartItemRepository;
import org.example.final_project.repository.ICartRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.ICartService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.final_project.specification.CartSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService implements ICartService {
    private final UserMapper userMapper;
    ICartRepository cartRepository;
    IUserRepository userRepository;
    CartMapper cartMapper;
    ICartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;

    @Override
    public CartDto getUserCart(Long userId) {
        return cartMapper.toDto(cartRepository.findOne(Specification.where(hasUserId(userId))).orElseGet(
                () -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(userRepository.findById(userId).orElseThrow(
                            () -> new IllegalArgumentException("User not found")
                    ));
                    newCart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                }
        ));
    }

    @Override
    public List<CartDto> getAll() {
        return List.of();
    }

    @Override
    public CartDto getById(Long id) {
        return null;
    }

    @Override
    public int save(CartModel cartModel) {
        return 0;
    }

    @Override
    public int update(Long aLong, CartModel cartModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public CheckoutDto getCheckOutDetail(Long cartId, List<Long> selectedCartItemIds) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
        List<CartItemEntity> selectedCartItems = new ArrayList<>();
        double totalAmount;
        if (cartEntity.isPresent()) {
            CartEntity cart = cartEntity.get();
            List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartId(cart.getCartId());

            if (selectedCartItemIds != null) {
                selectedCartItems = cartItemEntities.stream()
                        .filter(item -> selectedCartItemIds.contains(item.getCartDetailId()))
                        .toList();
                totalAmount = selectedCartItems.stream()
                        .mapToDouble(cartItem -> cartItem.getQuantity() * cartItem.getProduct().getPrice())
                        .sum();
            } else {
                selectedCartItems = new ArrayList<>();
                totalAmount = 0;
            }

            UserEntity userEntity = userRepository.findById(cart.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<CartItemDto> list = selectedCartItems.stream()
                    .map(cartItemMapper::toDto)
                    .toList();

            UserDto userDto = userMapper.toDto(userEntity);

            return CheckoutDto.builder()
                    .cartItems(list)
                    .userDto(userDto)
                    .totalPrice(totalAmount)
                    .build();
        }
        return null;
    }


}
