package org.example.final_project.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.entity.UserShippingAddressEntity;
import org.example.final_project.service.IShippingAddressService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressMapper {
    IShippingAddressService shippingAddressService;

    public ShippingAddressDto toDto(UserShippingAddressEntity userShippingAddressEntity) {
        return ShippingAddressDto.builder()
                .id(userShippingAddressEntity.getId())
                .addressLine1(shippingAddressService.buildAddressLine1(userShippingAddressEntity.getAddress()))
                .addressLine2(userShippingAddressEntity.getAddressLine2())
                .build();
    }
}
