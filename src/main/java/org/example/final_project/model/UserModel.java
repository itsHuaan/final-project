package org.example.final_project.model;

import lombok.*;
import org.example.final_project.enumeration.ShopStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserModel {
    private Long userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int gender = -1;
    private MultipartFile profilePicture;
    private Long roleId = 2L;
    private int isActive = 0;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Integer shop_status = ShopStatus.INACTIVE.getValue();
}
