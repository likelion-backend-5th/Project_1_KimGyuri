package com.example.MutsaMarket.entity;

import com.example.MutsaMarket.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "sales_item")
public class SalesItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="min_price_wanted")
    private Long minPriceWanted;

    private String status;
    private String writer;
    private String password;

    @OneToMany(mappedBy = "salesItem")
    private List<CommentEntity> comment;

    @OneToMany(mappedBy = "salesItem")
    private List<NegotiationEntity> negotiation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
