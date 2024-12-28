package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShippingAddressDto {
    private Long id;
    private String name;
    private String phone;
    private String addressLine1;
    private String addressLine2;
}
