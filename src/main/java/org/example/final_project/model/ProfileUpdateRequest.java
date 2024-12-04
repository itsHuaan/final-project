package org.example.final_project.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileUpdateRequest {
    private String name;
    private String phone;
    private String email;
    private int gender;
    private MultipartFile profilePicture;
}
