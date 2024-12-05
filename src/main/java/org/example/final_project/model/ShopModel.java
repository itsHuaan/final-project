package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopModel {

    private String shop_name;
    private int shop_address;
    private String shop_address_detail;
    private String phone;
}
