package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ShopRegisterRequest {
    private Long userId;
    private MultipartFile id_front;
    private MultipartFile id_back;
    private String tax_code;
    private String shop_name;
    private Long shop_address;
    private String shop_address_detail;
    private String phone;
    private String citizenIdentification;

}
