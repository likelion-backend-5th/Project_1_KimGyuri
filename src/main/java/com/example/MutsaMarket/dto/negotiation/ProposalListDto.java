package com.example.MutsaMarket.dto.negotiation;

import com.example.MutsaMarket.entity.NegotiationEntity;
import lombok.Data;

@Data
public class ProposalListDto {
    private Long id;
    private String username;
    private Long suggestedPrice;
    private String status;

    public static ProposalListDto fromEntity(NegotiationEntity entity) {
        ProposalListDto dto = new ProposalListDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUser().getUsername());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
