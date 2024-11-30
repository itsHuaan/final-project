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

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService implements IAddressService {
     IAddressRepository addressRepository;
     AddressMapper addressMapper;
    @Override
    public List<AddressDto> getAddressByParentId(long parentId) {
        List<AddressEntity> list =  addressRepository.findByParent_id(parentId);
        List<AddressDto> addressDtoList = list.stream().map(addressMapper::toAddressDto).toList();
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
    @Override
        public List<String> findAddressNamesFromParentId(Long parentId) {
        List<String> addressNames = new ArrayList<>();
        if(parentId != null) {
            AddressEntity addressEntity = addressRepository.findAddressEntitiesByParentId(parentId).get();
            addressNames.add(addressEntity.getName());
            findParentIdAddressNames(parentId, addressNames);
            return addressNames;
        }else {
            return null;
        }
    }

    private void findParentIdAddressNames(Long idAddress, List<String> addressNames) {
        Optional<AddressEntity> addressEntity = addressRepository.findById(idAddress);

        if (addressEntity.isPresent()) {
            AddressEntity address = addressEntity.get();
            addressNames.add(address.getName());
            if ( address.getParent_id() != 0) {
                findParentIdAddressNames(address.getParent_id(), addressNames);
            }
        }
    }
}
