package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddShippingAddressRequest {
    private String name;
    private String phone;
    private Long addressId;
    private String addressDetail;
}
