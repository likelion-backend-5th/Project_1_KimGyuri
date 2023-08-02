package com.example.MutsaMarket.exceptions;

public class Status500Exception extends RuntimeException{
    public Status500Exception(String message) {
        super(message);
    }
}
