package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_history_status_shipping")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HistoryStatusShippingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int status;
    private LocalDateTime createdChangeStatus;
    @ManyToOne
    @JoinColumn(name = "orderTracking_id")
    private OrderTrackingEntity orderTracking;


}
