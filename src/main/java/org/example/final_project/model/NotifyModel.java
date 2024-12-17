package org.example.final_project.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotifyModel {
    private Long shopId;
    private String notifyTitle;
    private long userId;
}
