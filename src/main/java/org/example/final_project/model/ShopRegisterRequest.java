package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ShopRegisterRequest {
    private Long userId;
    private String id_front;
    private String id_back;
    private String tax_code;
    private String shop_name;
    private int shop_address;
}
