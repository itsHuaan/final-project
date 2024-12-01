package org.example.final_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private Long id;
    private String type;
    private String token;
    private String username;
    private String name;
    private String email;
    private String roleName;
}
