package com.example.MutsaMarket.dto.comment;

import com.example.MutsaMarket.entity.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long itemId;

    //@NotBlank(message = "작성자를 입력해주세요.")
    private String writer;

    //@NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public static CommentDto fromEntity(CommentEntity entity) {
        CommentDto dto = new CommentDto();
        dto.setItemId(entity.getSalesItem().getId());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setContent(entity.getContent());
        return dto;
    }
}
