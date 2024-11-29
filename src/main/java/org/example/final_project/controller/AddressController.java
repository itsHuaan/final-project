package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Map;
@Tag( name = "ADDRESS")
@RestController
@RequestMapping(Const.API_PREFIX+"/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @Operation(summary = "Get All Address From ParentId ")
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<AddressDto>> getAddress(@PathVariable Long parentId) {
        List<AddressDto> list = addressService.getAddressByParentId(parentId);
        return ResponseEntity.ok(list);
    }
    @Operation(summary = "Find Address From ParentId")
    @GetMapping("/{parentId}")
    public ResponseEntity<?> findAddressFromParentId(@PathVariable Long parentId) {
        List<String>  map = addressService.findAddressNamesFromParentId(parentId);
        return ResponseEntity.ok(map);
    }
}
