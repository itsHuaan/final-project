package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_orderdetail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long shopId;
    private long quantity;
    private Double price;
    private long option1;
    private long option2;
    @ManyToOne
    @JoinColumn(name = "order_id" )
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private SKUEntity skuEntity;


}
