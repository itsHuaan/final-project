package org.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SKUModel {
    private long id;
    private String image;
    private long productId;
    private long optionId1;
    private long valueId1;
    private long optionId2;
    private long valueId2;
    private double price;
    private long quantity;
}
