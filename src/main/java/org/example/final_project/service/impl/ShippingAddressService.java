package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.mapper.ShippingAddressMapper;
import org.example.final_project.repository.IShippingAddressRepository;
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

    @Override
    public List<ShippingAddressDto> getShippingAddresses(Long userId) {
        return shippingAddressRepository.findAll(Specification.where(ofUser(userId))).stream()
                .map(shippingAddressMapper::toDto)
                .toList();
    }
}
