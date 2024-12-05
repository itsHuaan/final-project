package org.example.final_project.mapper;

import org.example.final_project.dto.CartUserDto;
import org.example.final_project.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class CartUserMapper {
    public CartUserDto toDto(UserEntity userEntity) {
        return CartUserDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}
