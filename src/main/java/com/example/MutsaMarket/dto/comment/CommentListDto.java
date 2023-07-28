package com.example.MutsaMarket.dto.comment;

import com.example.MutsaMarket.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentListDto {
    private Long id;
    private String username;
    private String content;
    private String reply;

    public static CommentListDto fromEntity(CommentEntity entity) {
        CommentListDto dto = new CommentListDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUser().getUsername());
        dto.setContent(entity.getContent());
        dto.setReply(entity.getReply());
        return dto;
    }
}
