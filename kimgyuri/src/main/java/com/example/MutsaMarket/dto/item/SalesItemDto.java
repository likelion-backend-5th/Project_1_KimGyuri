package com.example.MutsaMarket.dto.item;

import com.example.MutsaMarket.entity.SalesItemEntity;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalesItemDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "물품 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "가격을 입력해주세요.")
    private Long minPriceWanted;

    //@NotBlank(message = "작성자를 입력해주세요.")
    private String writer;

    //@NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public static SalesItemDto fromEntity(SalesItemEntity entity) {
        SalesItemDto dto = new SalesItemDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        return dto;
    }
}
