package org.example.final_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteProductDto {
    UserDto user;
    ProductSummaryDto product;
}
