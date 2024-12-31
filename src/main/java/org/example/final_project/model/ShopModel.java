package org.example.final_project.model;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopModel {

    private String shop_name;
    private Long shop_address;
    private String shop_address_detail;
    private String phone;
}
