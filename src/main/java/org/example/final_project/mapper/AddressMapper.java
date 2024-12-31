package org.example.final_project.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.AddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.model.AddressModel;
import org.example.final_project.repository.IAddressRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressMapper {
    IAddressRepository addressRepository;

    public AddressDto toAddressDto(AddressEntity address) {
        return AddressDto.builder()
                .id(address.getId())
                .name(address.getName())
                .parentId(address.getParent_id())
                .build();
    }

    public AddressEntity toEntity(AddressModel addressModel) {
        return AddressEntity.builder()
                .id(addressModel.getId())
                .name(addressModel.getName())
                .parent_id(addressModel.getParentId())
                .build();
    }

    public String buildAddressLine(AddressEntity address) {
        StringBuilder fullAddress = new StringBuilder();
        AddressEntity currentAddress = address;
        while (currentAddress != null) {
            if (!fullAddress.isEmpty()) {
                fullAddress.append(", ");
            }
            fullAddress.append(currentAddress.getName());
            long parentId = currentAddress.getParent_id();
            currentAddress = (parentId != 0)
                    ? addressRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Parent address not found"))
                    : null;
        }
        return fullAddress.toString();
    }

}
