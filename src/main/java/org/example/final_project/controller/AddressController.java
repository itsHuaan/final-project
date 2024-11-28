package org.example.final_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.AddressDto;
import org.example.final_project.service.impl.AddressService;
import org.example.final_project.util.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Const.API_PREFIX+"/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<AddressDto>> getAddress(@PathVariable Long parentId) {
        List<AddressDto> list = addressService.getAddressByParentId(parentId);
        return ResponseEntity.ok(list);
    }
}
