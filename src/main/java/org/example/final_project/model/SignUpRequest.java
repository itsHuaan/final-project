package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
}
