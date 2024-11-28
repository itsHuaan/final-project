package org.example.final_project.service;

import org.example.final_project.dto.AddressDto;
import org.example.final_project.model.AddressModel;

import java.util.List;

public interface IAddressService extends IBaseService<AddressDto, AddressModel,Long> {
    List<AddressDto> getAddressByParentId(long parentId);
}
