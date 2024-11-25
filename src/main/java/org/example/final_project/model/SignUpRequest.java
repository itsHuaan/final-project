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
    private String username;
    private String password;
    private String email;
    private Long roleId;
}
