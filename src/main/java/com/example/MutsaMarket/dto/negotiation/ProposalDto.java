package com.example.MutsaMarket.dto.negotiation;

import com.example.MutsaMarket.entity.NegotiationEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProposalDto {
    //@NotBlank(message = "작성자를 입력해주세요.")
    private String writer;

    //@NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "제안 가격을 입력해주세요.")
    private Long suggestedPrice;

    public static ProposalDto fromEntity(NegotiationEntity entity) {
        ProposalDto dto = new ProposalDto();
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        return dto;
    }
}
