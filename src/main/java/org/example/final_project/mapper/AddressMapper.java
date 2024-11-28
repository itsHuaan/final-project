package org.example.final_project.mapper;

import jakarta.mail.Address;
import org.example.final_project.dto.AddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.model.AddressModel;

public class AddressMapper {
    public static AddressDto toAddressDto(AddressEntity address) {
        return AddressDto.builder()
                .id(address.getId())
                .name(address.getName())
                .parentId(address.getParent_id())
                .build();
    }
    public static AddressEntity toEntity(AddressModel addressModel) {
        return AddressEntity.builder()
                .id(addressModel.getId())
                .name(addressModel.getName())
                .parent_id(addressModel.getParentId())
                .build();
    }
}
