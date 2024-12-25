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
    private String profilePicture;
    private String shopAddress;
    private String shopAddressDetail;
    private Double rating;
    private Long productCount;
    private long sold;
    private Long joined;
    private Long feedbackCount;
}
