package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel {
    String recipient;
    String subject;
    String content;
}
