package com.example.MutsaMarket.entity;

import com.example.MutsaMarket.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItemEntity salesItem;

    private String writer;
    private String password;
    private String content;
    private String reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
