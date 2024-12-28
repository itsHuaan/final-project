package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_ordertracking")
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
    private String note;
    private long shopId;
    private LocalDateTime paidDate;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @OneToMany(mappedBy = "orderTracking")
    private List<HistoryStatusShippingEntity> historyStatusShippingEntities;


}
