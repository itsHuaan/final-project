package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopDto {
    private Long shopId;
    private String shopName;
    private String shopAddress;
    private String shopAddressDetail;
}
