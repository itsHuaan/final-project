package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateShippingAddressRequest {
    private Long oldAddressId;
    private Long newAddressId;
    private String addressDetail;
}
