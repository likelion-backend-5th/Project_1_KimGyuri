package com.example.MutsaMarket.exceptions;

public class ItemNotFoundException extends Status404Exception{
    public ItemNotFoundException() {
        super("해당 물품을 찾을 수 없습니다.");
    }
}
