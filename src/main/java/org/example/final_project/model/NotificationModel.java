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
    private long recipientId;
    private long senderId;
    private String image;


}
