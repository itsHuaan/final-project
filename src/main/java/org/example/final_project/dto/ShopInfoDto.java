package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShopInfoDto {
    private String shopName;
    private Long productCount;
    private Long joined;
    private Long feedbackCount;
}
