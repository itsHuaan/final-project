package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderTrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int status;
    private LocalDateTime createdAt;
    private int createdBy;
    private String note;
    private LocalDateTime paidDate;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

}
