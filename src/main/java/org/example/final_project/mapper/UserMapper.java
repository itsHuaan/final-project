package org.example.final_project.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.CartUserDto;
import org.example.final_project.dto.ShopDto;
import org.example.final_project.dto.UserDto;
import org.example.final_project.dto.UserFeedBackDto;
import org.example.final_project.entity.OrderDetailEntity;
import org.example.final_project.entity.ProductEntity;
import org.example.final_project.entity.RoleEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.model.UserModel;
import org.example.final_project.repository.IOrderDetailRepository;
import org.example.final_project.repository.IProductRepository;
import org.example.final_project.service.IAddressService;
import org.example.final_project.specification.OrderDetailSpecification;
import org.example.final_project.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserMapper {
    ShippingAddressMapper shippingAddressMapper;
    IOrderDetailRepository orderDetailRepository;
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
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        ProductSpecification.hasUserId(userEntity.getUserId()))
                .and(ProductSpecification.isValid())
                .and(ProductSpecification.isStatus(1)));

        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll(Specification.where(
                OrderDetailSpecification.hasShop(userEntity.getUserId())
                        .and(OrderDetailSpecification.isValid())));

        Map<ProductEntity, Long> productQuantities = orderDetails.stream()
                .filter(orderDetail -> products.contains(orderDetail.getSkuEntity().getProduct()))
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getSkuEntity().getProduct(),
                        Collectors.summingLong(OrderDetailEntity::getQuantity)
                ));

        double totalWeightedRating = 0.0;
        long totalSoldQuantity = 0;
        for (ProductEntity product : products) {
            long productSoldQuantity = productQuantities.getOrDefault(product, 0L);
            if (productSoldQuantity > 0) {
                double productWeightedRating = product.getFeedbacks().stream()
                        .mapToDouble(feedback -> feedback.getRate() * productSoldQuantity)
                        .sum();
                totalWeightedRating += productWeightedRating;
                totalSoldQuantity += productSoldQuantity;
            }
        }

        double averageRating = totalSoldQuantity > 0
                ? totalWeightedRating / totalSoldQuantity
                : 0.0;

        LocalDateTime createdTime = userEntity.getTime_created_shop();
        Duration duration = createdTime != null
                ? Duration.between(createdTime, LocalDateTime.now())
                : Duration.ZERO;

        return ShopDto.builder()
                .shopId(userEntity.getUserId())
                .shopName(userEntity.getShop_name())
                .shopAddress(String.join(", ", addressService.findAddressNamesFromParentId(
                        Long.parseLong(String.valueOf(userEntity.getAddress_id_shop())))))
                .shopAddressDetail(userEntity.getShop_address_detail())
                .feedbackCount(products.stream()
                        .mapToLong(product -> product.getFeedbacks().size())
                        .sum())
                .productCount((long) products.size())
                .joined(duration.toDays())
                .profilePicture(userEntity.getProfilePicture())
                .rating(Math.round(averageRating * 100.0) / 100.0)
                .sold(totalSoldQuantity)
                .build();
    }


/*
    public ShopDto toShopDto(UserEntity userEntity) {
        List<ProductEntity> products = productRepository.findAll(Specification.where(
                        hasUserId(userEntity.getUserId()))
                .and(isValid())
                .and(isStatus(1)));
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll(Specification.where(
                OrderDetailSpecification.hasShop(userEntity.getUserId())
                        .and(OrderDetailSpecification.isValid())));
        Map<ProductEntity, Long> productQuantities = orderDetails.stream()
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getSkuEntity().getProduct(),
                        Collectors.summingLong(OrderDetailEntity::getQuantity)
                ));
        double totalWeightedRating = 0.0;
        long totalSoldQuantity = 0;
        for (ProductEntity product : products) {
            long productSoldQuantity = productQuantities.getOrDefault(product, 0L);
            if (productSoldQuantity > 0) {
                double productWeightedRating = product.getFeedbacks().stream()
                        .mapToDouble(feedback -> feedback.getRate() * productSoldQuantity)
                        .sum();
                totalWeightedRating += productWeightedRating;
                totalSoldQuantity += productSoldQuantity;
            }
        }
        double averageRating = totalSoldQuantity > 0
                ? totalWeightedRating / totalSoldQuantity
                : 0.0;
        LocalDateTime createdTime = userEntity.getTime_created_shop();
        Duration duration = createdTime != null
                ? Duration.between(createdTime, LocalDateTime.now())
                : Duration.ZERO;
        return ShopDto.builder()
                .shopId(userEntity.getUserId())
                .shopName(userEntity.getShop_name())
                .shopAddress(String.join(", ", addressService.findAddressNamesFromParentId(
                        Long.parseLong(String.valueOf(userEntity.getAddress_id_shop())))))
                .shopAddressDetail(userEntity.getShop_address_detail())
                .feedbackCount(products.stream()
                        .mapToLong(product -> product.getFeedbacks().size())
                        .sum())
                .productCount((long) products.size())
                .joined(duration.toDays())
                .profilePicture(userEntity.getProfilePicture())
                .rating(Math.round(averageRating * 100.0) / 100.0)
                .sold(totalSoldQuantity)
                .build();
    }
*/

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
}
