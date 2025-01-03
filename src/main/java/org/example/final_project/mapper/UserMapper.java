package org.example.final_project.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.*;
import org.example.final_project.entity.*;
import org.example.final_project.model.UserModel;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserMapper {
    ShippingAddressMapper shippingAddressMapper;
    IProductRepository productRepository;
    IAddressRepository addressRepository;
    AddressMapper addressMapper;

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
        List<ProductDto> products = productRepository.findAll(Specification.where(
                                ProductSpecification.hasUserId(userEntity.getUserId()))
                        .and(ProductSpecification.isValid())
                        .and(ProductSpecification.isStatus(1))).stream()
                .map(this::convertToDto)
                .toList();
        long totalSold = products.stream()
                .mapToLong(ProductDto::getSold)
                .sum();
        double averageRating = products.stream()
                .mapToDouble(product -> product.getSold() * product.getRating())
                .sum() / totalSold;
        LocalDateTime createdTime = userEntity.getTime_created_shop();
        Duration duration = createdTime != null
                ? Duration.between(createdTime, LocalDateTime.now())
                : Duration.ZERO;
        AddressEntity address = addressRepository.findById(userEntity.getAddress_id_shop()).orElse(null);
        return ShopDto.builder()
                .shopId(userEntity.getUserId())
                .shopName(userEntity.getShop_name())
                .shopAddress(address != null
                        ? addressMapper.buildAddressLine(address)
                        : null)
                .shopAddressDetail(userEntity.getShop_address_detail())
                .feedbackCount(products.stream()
                        .mapToLong(product -> product.getFeedbacks().size())
                        .sum())
                .productCount((long) products.size())
                .joined(duration.toDays())
                .profilePicture(userEntity.getProfilePicture())
                .rating(Math.round(averageRating * 100.0) / 100.0)
                .sold(totalSold)
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

    public CartUserDto toCartUserDto(UserEntity userEntity) {
        return CartUserDto.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }

    private ProductDto convertToDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .productId(productEntity.getId())
                .productName(productEntity.getName())
                .numberOfLike(productEntity.getFavorites().size())
                .numberOfFeedBack(productEntity.getFeedbacks().size())
                .rating(productEntity.getFeedbacks().stream()
                        .mapToDouble(FeedbackEntity::getRate)
                        .average()
                        .orElse(0.0))
                .feedbacks(productEntity.getFeedbacks().stream()
                        .sorted(Comparator.comparing(FeedbackEntity::getCreatedAt).reversed())
                        .map(this::convertToDto)
                        .toList())
                .sold(productEntity.getSkuEntities().stream()
                        .flatMap(sku -> sku.getOrderDetails().stream())
                        .mapToLong(OrderDetailEntity::getQuantity)
                        .sum())
                .build();
    }

    private FeedbackDto convertToDto(FeedbackEntity feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .rate(feedback.getRate())
                .replyFromSeller(feedback.getReplyFromSeller())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
