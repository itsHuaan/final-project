package org.example.final_project.service;

import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.entity.AddressEntity;

import java.util.List;

public interface IShippingAddressService{
    List<ShippingAddressDto> getShippingAddresses(Long userId);
    String buildAddressLine1(AddressEntity address);
}
