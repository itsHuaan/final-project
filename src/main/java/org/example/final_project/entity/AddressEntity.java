package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long parent_id;
}

