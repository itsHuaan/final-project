package org.example.final_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.entity.UserEntity;
import org.example.final_project.entity.UserShippingAddressEntity;
import org.example.final_project.mapper.ShippingAddressMapper;
import org.example.final_project.model.AddShippingAddressRequest;
import org.example.final_project.model.UpdateShippingAddressRequest;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IShippingAddressRepository;
import org.example.final_project.repository.IUserRepository;
import org.example.final_project.service.IShippingAddressService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.final_project.specification.ShippingAddressSpecification.ofUser;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService implements IShippingAddressService {
    IShippingAddressRepository shippingAddressRepository;
    ShippingAddressMapper shippingAddressMapper;
    IUserRepository userRepository;
    IAddressRepository addressRepository;

    @Override
    public List<ShippingAddressDto> getShippingAddresses(Long userId) {
        return shippingAddressRepository.findAll(Specification.where(ofUser(userId))).stream()
                .map(shippingAddressMapper::toDto)
                .toList();
    }

    @Override
    public int addAddress(long userId, AddShippingAddressRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getShippingAddresses().size() >= 20) {
            throw new IllegalArgumentException("Cannot add more than 20 shipping addresses");
        }

        shippingAddressRepository.save(shippingAddressMapper.toEntity(userId, request));
        return 1;
    }

    @Override
    public int updateAddress(long userId, UpdateShippingAddressRequest request) {
        AddressEntity address = request.getAddressId() != null
                ? addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found"))
                : null;
        UserShippingAddressEntity currentShippingAddress = shippingAddressRepository.findById(request.getShippingAddressId()).orElseThrow(() -> new EntityNotFoundException("User shipping address not found"));

        currentShippingAddress.setName(request.getName() != null ? request.getName() : currentShippingAddress.getName());
        currentShippingAddress.setPhone(request.getPhone() != null ? request.getPhone() : currentShippingAddress.getPhone());
        currentShippingAddress.setAddress(address != null ? address : currentShippingAddress.getAddress());
        currentShippingAddress.setAddressDetail(request.getAddressDetail() != null ? request.getAddressDetail() : currentShippingAddress.getAddressDetail());

        shippingAddressRepository.save(currentShippingAddress);
        return 1;
    }

    @Override
    public int deleteAddress(long userId, Long shippingAddressId) {
        UserShippingAddressEntity currentShippingAddress = shippingAddressRepository.findById(shippingAddressId).orElseThrow(() -> new EntityNotFoundException("User shipping address not found"));
        shippingAddressRepository.delete(currentShippingAddress);
        return 1;
    }
}
