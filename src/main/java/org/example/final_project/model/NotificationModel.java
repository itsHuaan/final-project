package org.example.final_project.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationModel {
    private String title;
    private String content;
    private Long userId;
    private Long adminId;
    private Long shopId;
    private String image;
    private String orderCode;

}
