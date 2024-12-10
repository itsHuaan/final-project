package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SKUModel {
    private long id;
    private String image;
    private Long productId;
    private Long optionId1;
    private Long valueId1;
    private Long optionId2;
    private Long valueId2;
    private double price;
    private long quantity;
}
