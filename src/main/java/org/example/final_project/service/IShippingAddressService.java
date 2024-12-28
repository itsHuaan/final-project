package org.example.final_project.service;

import org.example.final_project.dto.ShippingAddressDto;
import org.example.final_project.model.AddShippingAddressRequest;
import org.example.final_project.model.UpdateShippingAddressRequest;

import java.util.List;

public interface IShippingAddressService {
    List<ShippingAddressDto> getShippingAddresses(Long userId);

    int addAddress(long userId, AddShippingAddressRequest request);

    int updateAddress(long userId, UpdateShippingAddressRequest request);

    int deleteAddress(long userId, Long addressId);
}
