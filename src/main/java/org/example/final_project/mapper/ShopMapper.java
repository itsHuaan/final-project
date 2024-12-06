package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShopDto;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.service.IAddressService;
import org.example.final_project.service.IShippingAddressService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopMapper {
    IAddressService addressService;

    public ShopDto toDto(UserEntity userEntity) {
        return ShopDto.builder()
                .shopId(userEntity.getUserId())
                .shopName(userEntity.getShop_name())
                .shopAddress(String.join(", ", addressService.findAddressNamesFromParentId(Long.parseLong(String.valueOf(userEntity.getAddress_id_shop())))))
                .shopAddressDetail(userEntity.getShop_address_detail())
                .build();
    }
}
