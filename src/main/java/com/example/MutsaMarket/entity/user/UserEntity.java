package com.example.MutsaMarket.entity.user;

import com.example.MutsaMarket.entity.CommentEntity;
import com.example.MutsaMarket.entity.NegotiationEntity;
import com.example.MutsaMarket.entity.SalesItemEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "user")
    private List<SalesItemEntity> salesItem;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comment;

    @OneToMany(mappedBy = "user")
    private List<NegotiationEntity> negotiation;
}