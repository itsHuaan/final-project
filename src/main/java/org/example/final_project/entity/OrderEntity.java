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
    private Double total_price;
    private String shipping_address;
    private String status_checkout;
    private String method_checkout;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false  )
    private UserEntity user;

    @OneToMany(mappedBy = "orderEntity")
    private List<OrderDetailEntity> orderDetailEntities;


}
