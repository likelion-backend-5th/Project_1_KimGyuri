package com.example.MutsaMarket.exceptions;

public class NegotiationNotFoundException extends Status404Exception{
    public NegotiationNotFoundException() {
        super("해당 구매 제안을 찾을 수 없습니다.");
    }
}
