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
    private MultipartFile file;
    private long productId;
    private long optionId;
    private long valueId;
    private double price;
    private long quantity;

}
