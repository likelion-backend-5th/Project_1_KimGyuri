package com.example.MutsaMarket.exceptions;

public abstract class Status403Exception extends RuntimeException {
    public Status403Exception(String message) {
        super(message);
    }
}
