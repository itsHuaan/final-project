package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDto {
    private long id;
    private long shopId;
    private long quantity;
    private Double price;
    private long option1;
    private long option2;
    private SKUDto skuDto;

}
