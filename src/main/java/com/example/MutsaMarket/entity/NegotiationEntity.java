package com.example.MutsaMarket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="negotiation")
public class NegotiationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private SalesItemEntity salesItem;

    @Column(name="suggested_price")
    private Long suggestedPrice;

    private String status;
    private String writer;
    private String password;
}
