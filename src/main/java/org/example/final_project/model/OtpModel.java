package org.example.final_project.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpModel {
    private long id;
    private String otpCode;
    private String email;
    private LocalDateTime createdAt = LocalDateTime.now();
    private int status = 1;
}
