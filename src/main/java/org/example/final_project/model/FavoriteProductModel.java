package org.example.final_project.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavoriteProductModel {
    private Long userId;
    private Long productId;
}
