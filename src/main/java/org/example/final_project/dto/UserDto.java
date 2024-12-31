package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String phone;
    private List<ShippingAddressDto> addresses;
    private int gender;
    private Long roleId;
    private String id_front;
    private String id_back;
    private String tax_code;
    private String shop_name;
    private int isActive;
    private String activationNote;
    private Long address_id_shop;
    private Integer shop_status;
    private String shop_address_detail;
    private LocalDateTime time_created_shop;
    private List<String> allAddresses;
    private String profilePicture;
}
