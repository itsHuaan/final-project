package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private Double totalPrice;
    private String shippingAddress;
    private long statusCheckout;
    private String methodCheckout;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String orderCode;
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false  )
    private UserEntity user;

    @OneToMany(mappedBy = "orderEntity")
    private List<OrderDetailEntity> orderDetailEntities;

    @OneToMany(mappedBy = "order")
    private List<OrderTrackingEntity> orderTrackingEntities;


}
