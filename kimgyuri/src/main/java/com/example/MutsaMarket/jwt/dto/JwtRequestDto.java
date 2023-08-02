package com.example.MutsaMarket.jwt.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
