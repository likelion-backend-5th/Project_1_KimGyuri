package com.example.MutsaMarket.dto.comment;

import com.example.MutsaMarket.entity.CommentEntity;
import lombok.Data;

@Data
public class ReplyDto {
    private String writer;
    private String password;
    private String reply;

    public static ReplyDto fromEntity(CommentEntity entity) {
        ReplyDto dto = new ReplyDto();
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setReply(entity.getReply());
        return dto;
    }
}
