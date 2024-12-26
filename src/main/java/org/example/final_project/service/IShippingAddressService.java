package org.example.final_project.service;

import org.example.final_project.dto.ShippingAddressDto;

import java.util.List;

public interface IShippingAddressService {
    List<ShippingAddressDto> getShippingAddresses(Long userId);
}
