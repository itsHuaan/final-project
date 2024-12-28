package org.example.final_project.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.entity.UserShippingAddressEntity;
import org.example.final_project.model.AddShippingAddressRequest;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IUserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressMapper {
    AddressMapper addressMapper;
    IUserRepository userRepository;
    IAddressRepository addressRepository;

    public UserShippingAddressEntity toEntity(Long userId, AddShippingAddressRequest shippingAddressRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        AddressEntity address = addressRepository.findById(shippingAddressRequest.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        return UserShippingAddressEntity.builder()
                .user(user)
                .address(address)
                .name(shippingAddressRequest.getName())
                .phone(shippingAddressRequest.getPhone())
                .addressDetail(shippingAddressRequest.getAddressDetail())
                .build();
    }

    public ShippingAddressDto toDto(UserShippingAddressEntity userShippingAddressEntity) {
        return ShippingAddressDto.builder()
                .id(userShippingAddressEntity.getId())
                .name(userShippingAddressEntity.getName())
                .phone(userShippingAddressEntity.getPhone())
                .addressLine1(addressMapper.buildAddressLine(userShippingAddressEntity.getAddress()))
                .addressLine2(userShippingAddressEntity.getAddressDetail())
                .build();
    }
}
