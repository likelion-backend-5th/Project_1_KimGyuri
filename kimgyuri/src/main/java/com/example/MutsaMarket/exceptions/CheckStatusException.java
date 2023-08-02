package com.example.MutsaMarket.exceptions;

public class CheckStatusException extends Status400Exception{
    public CheckStatusException() {
        super("구매 제안 상태를 확인해주세요.");
    }
}
