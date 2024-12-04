package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SendForgotPasswordEmailRequest {
    private String email;
}
