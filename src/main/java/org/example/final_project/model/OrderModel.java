package org.example.final_project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderModel {
    private int user_id;
    private String city;
    private String district;
    private String town;
    private String address_detail ;
    private String phone;
    private String email;
    private String total_price;
}
