package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

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
    private String password;
    private String email;
    private String phone;
    private int gender;
    private Long roleId;
    private String id_front;
    private String id_back;
    private String tax_code;
    private String shop_name;
    private int shop_address;
    private Integer shop_status;
    private String shop_address_detail;
    private LocalDateTime time_created_shop;



    private String profilePicture;
}
