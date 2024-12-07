package org.example.final_project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserFeedBackDto {
    private Long userId;
    private String name;
    private String username;
    private String profilePicture;
}
