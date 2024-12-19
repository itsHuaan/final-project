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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService implements IAddressService {
    IAddressRepository addressRepository;
    AddressMapper addressMapper;


    @Override
    public List<AddressDto> getAddressByParentId(long parentId) {
        return addressRepository.findByParent_id(parentId).stream()
                .map(addressMapper::toAddressDto)
                .toList();
    }

    @Override
    public List<AddressDto> getAll() {
        return List.of();
    }

    @Override
    public AddressDto getById(Long id) {
        if (addressRepository.findById(id).isPresent()) {
            return addressMapper.toAddressDto(addressRepository.findById(id).get());
        } else {
            return null;
        }
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
    public List<String> findAddressNamesFromParentId(Long address_id) {
        List<String> addressNames = new ArrayList<>();
        if (address_id != null) {
            findParentIdAddressNames(address_id, addressNames);
            return addressNames;
        } else {
            return null;
        }
    }

    private void findParentIdAddressNames(Long idAddress, List<String> addressNames) {
        Optional<AddressEntity> addressEntity = addressRepository.findById(idAddress);

        if (addressEntity.isPresent()) {
            AddressEntity address = addressEntity.get();
            addressNames.add(address.getName());
            if (address.getParent_id() != 0) {
                findParentIdAddressNames(address.getParent_id(), addressNames);
            }
        }
    }
}
