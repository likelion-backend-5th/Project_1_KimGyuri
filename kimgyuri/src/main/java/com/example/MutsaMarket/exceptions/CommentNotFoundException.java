package com.example.MutsaMarket.exceptions;

public class CommentNotFoundException extends Status404Exception{
    public CommentNotFoundException() {
        super("해당 댓글을 찾을 수 없습니다.");
    }
}
