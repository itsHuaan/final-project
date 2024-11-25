package org.example.final_project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpDto {
    private long id;
    private String otpCode;
    private String email;
    private LocalDateTime createdAt;
    private int status = 1;
}
