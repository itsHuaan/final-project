package org.example.final_project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserInfoDto {
    private String number;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String issueDate;
}
