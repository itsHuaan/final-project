package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.AddressDto;
import org.example.final_project.entity.AddressEntity;
import org.example.final_project.mapper.AddressMapper;
import org.example.final_project.model.AddressModel;
import org.example.final_project.repository.IAddressRepository;
import org.example.final_project.service.IAddressService;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService implements IAddressService {
     IAddressRepository addressRepository;
    @Override
    public List<AddressDto> getAddressByParentId(long parentId) {
        List<AddressEntity> list =  addressRepository.findByParent_id(parentId);
        List<AddressDto> addressDtoList = list.stream().map(e-> AddressMapper.toAddressDto(e)).toList();
        return addressDtoList;
    }

    @Override
    public List<AddressDto> getAll() {
        return List.of();
    }

    @Override
    public AddressDto getById(Long id) {
        return null;
    }

    @Override
    public int save(AddressModel addressModel) {
        return 0;
    }

    @Override
    public int update(Long aLong, AddressModel addressModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
