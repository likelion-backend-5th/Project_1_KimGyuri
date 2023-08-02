package com.example.MutsaMarket.exceptions;

public class ImageUploadException extends Status500Exception{
    public ImageUploadException() {
        super("이미지 업로드 과정에서 오류가 발생했습니다.");
    }
}
