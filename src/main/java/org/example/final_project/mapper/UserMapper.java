package org.example.final_project.mapper;

import org.example.final_project.dto.UserDto;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roleId(userEntity.getRole() != null ? userEntity.getRole().getRoleId() : null)
                .id_back(userEntity.getId_back())
                .id_front(userEntity.getId_front())
                .shop_name(userEntity.getShop_name())
                .shop_status(userEntity.getShop_status())
                .tax_code(userEntity.getTax_code())
                .profilePicture(userEntity.getProfilePicture())
                .build();
    }

    public UserEntity toEntity(UserModel userModel) {
        RoleEntity role = new RoleEntity();
        role.setRoleId(userModel.getRoleId());
        return UserEntity.builder()
                .userId(userModel.getUserId())
                .name(userModel.getName())
                .username(userModel.getUsername())
                .email(userModel.getEmail())
                .password(userModel.getPassword())
                .isActive(userModel.getIsActive())
                .role(role)
                .createdAt(userModel.getCreatedAt())
                .shop_status(userModel.getShop_status())
                .build();
    }
}
