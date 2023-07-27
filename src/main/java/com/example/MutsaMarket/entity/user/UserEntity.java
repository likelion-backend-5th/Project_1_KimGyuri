package com.example.MutsaMarket.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false, unique = true)
    private String username;
    private String password;

    private String phone;
    private String email;
    private String address;
}