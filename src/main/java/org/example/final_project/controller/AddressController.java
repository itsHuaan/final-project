package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.final_project.dto.AddressDto;
import org.example.final_project.service.impl.AddressService;
import org.example.final_project.util.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.example.final_project.dto.ApiResponse.createResponse;

@Tag(name = "ADDRESS")
@RestController
@RequestMapping(Const.API_PREFIX + "/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Get All Address From ParentId")
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<AddressDto>> getAddress(@PathVariable Long parentId) {
        List<AddressDto> list = addressService.getAddressByParentId(parentId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Find full address by id")
    @GetMapping("/{id}/full-address")
    public ResponseEntity<?> findAddressFromParentId(@PathVariable Long id) {
        List<String> map = addressService.findAddressNamesFromParentId(id);
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Find address by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        AddressDto addressDto = addressService.getById(id);
        HttpStatus status = HttpStatus.OK;
        String message = "Address fetched";
        if (addressDto == null) {
            status = HttpStatus.NOT_FOUND;
            message = "No address fetched";
        }
        return ResponseEntity.status(status).body(createResponse(
                status,
                message,
                addressDto));
    }
}
