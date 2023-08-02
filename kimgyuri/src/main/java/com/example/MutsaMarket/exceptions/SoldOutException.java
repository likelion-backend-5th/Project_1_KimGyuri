package com.example.MutsaMarket.exceptions;

public class SoldOutException extends Status400Exception{
    public SoldOutException() {
        super("이미 판매 완료된 물품입니다.");
    }
}
