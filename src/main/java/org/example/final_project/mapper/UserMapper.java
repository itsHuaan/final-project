package org.example.final_project.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShopDto;
import org.example.final_project.dto.ShopInfoDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.dto.UserFeedBackDto;
import org.example.final_project.entity.FeedbackEntity;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.UserModel;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IAddressService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.util.specification.ProductSpecification.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserMapper {
    ShippingAddressMapper shippingAddressMapper;

    IAddressService addressService;
    IProductRepository productRepository;

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .roleId(userEntity.getRole() != null ? userEntity.getRole().getRoleId() : null)
                .id_back(userEntity.getId_back())
                .id_front(userEntity.getId_front())
                .shop_name(userEntity.getShop_name())
                .shop_status(userEntity.getShop_status())
                .tax_code(userEntity.getTax_code())
                .time_created_shop(userEntity.getTime_created_shop())
                .profilePicture(userEntity.getProfilePicture())
                .gender(userEntity.getGender())
                .phone(userEntity.getPhone())
                .isActive(userEntity.getIsActive())
                .activationNote(userEntity.getActivationNote())
                .address_id_shop(userEntity.getAddress_id_shop())
                .shop_address_detail(userEntity.getShop_address_detail())
                .time_created_shop(userEntity.getTime_created_shop())
                .profilePicture(userEntity.getProfilePicture())
                .addresses(userEntity.getShippingAddresses().stream().map(shippingAddressMapper::toDto).toList())
                .shopInfo(userEntity.getShop_status() == 1
                        ? toShopInfoDto(userEntity)
                        : null)
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
                .phone(userModel.getPhone())
                .gender(userModel.getGender())
                .build();
    }

    public ShopDto toShopDto(UserEntity userEntity) {
        return ShopDto.builder()
                .shopId(userEntity.getUserId())
                .shopName(userEntity.getShop_name())
                .shopAddress(String.join(", ", addressService.findAddressNamesFromParentId(Long.parseLong(String.valueOf(userEntity.getAddress_id_shop())))))
                .shopAddressDetail(userEntity.getShop_address_detail())
                .build();
    }

    public UserFeedBackDto toUserFeedBackDto(UserEntity userEntity) {
        return UserFeedBackDto.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .profilePicture(userEntity.getProfilePicture())
                .build();
    }

    public ShopInfoDto toShopInfoDto(UserEntity userEntity) {
        List<FeedbackEntity> feedbacks = productRepository.findAll(Specification.where(hasUserId(userEntity.getUserId())))
                .stream()
                .flatMap(product -> product.getFeedbacks().stream())
                .toList();
        Duration joined = Duration.between(userEntity.getTime_created_shop(), LocalDateTime.now());
        return ShopInfoDto.builder()
                .shopName(userEntity.getShop_name())
                .feedbackCount((long) feedbacks.size())
                .productCount((long) productRepository.findAll(Specification.where(hasUserId(userEntity.getUserId())))
                        .size())
                .joined(joined.toDays())
                .build();
    }
}
