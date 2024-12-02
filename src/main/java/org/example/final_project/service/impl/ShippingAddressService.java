package org.example.final_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.repository.IShippingAddressRepository;
import org.example.final_project.service.IShippingAddressService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.final_project.util.specification.ShippingAddressSpecification.ofUser;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService implements IShippingAddressService {
    IShippingAddressRepository shippingAddressRepository;
    IAddressRepository addressRepository;

    @Override
    public List<ShippingAddressDto> getShippingAddresses(Long userId) {
        return shippingAddressRepository.findAll(Specification.where(ofUser(userId))).stream()
                .map(entity -> ShippingAddressDto.builder()
                        .addressLine1(buildAddressLine1(entity.getAddress()))
                        .addressLine2(entity.getAddressLine2())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String buildAddressLine1(AddressEntity address) {
        StringBuilder addressLine1 = new StringBuilder(address.getName());
        long parentId = address.getParent_id();

        while (parentId != 0) {
            AddressEntity parent = addressRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent address not found"));
            addressLine1.insert(0, parent.getName() + ", ");
            parentId = parent.getParent_id();
        }
        return addressLine1.toString();
    }
}
