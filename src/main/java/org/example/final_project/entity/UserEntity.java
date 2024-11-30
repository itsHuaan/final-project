package org.example.final_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tbl_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;

    @Column(unique=true)
    private String username;
    private String password;

    @Column(unique=true)
    private String email;
    private int isActive;
    private String profilePicture;
    private String phone;
    private int gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user")
    private List<FeedbackEntity> feedbacks;
    private String id_front;
    private String id_back;
    private String tax_code;
    private String shop_name;
    private Integer shop_status;
    private String shop_address_detail;
    private LocalDateTime time_created_shop;
    private int address_id;
    @OneToMany(mappedBy = "user")
    private List<CategoryEntity> categories;
    @OneToMany(mappedBy = "user")
    private List<ProductEntity> products;
}
