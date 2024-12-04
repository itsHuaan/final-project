package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartDto;
import org.example.final_project.entity.CartEntity;
import org.example.final_project.mapper.CartMapper;
import org.example.final_project.model.CartModel;
import org.example.final_project.repository.ICartRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.ICartService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.util.specification.CartSpecification.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService implements ICartService {
    ICartRepository cartRepository;
    IUserRepository userRepository;
    CartMapper cartMapper;

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
}
