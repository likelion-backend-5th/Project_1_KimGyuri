package com.example.MutsaMarket.exceptions;

public class UserNotFoundException extends Status404Exception{
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
