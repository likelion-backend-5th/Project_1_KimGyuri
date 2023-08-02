package com.example.MutsaMarket.dto.negotiation;

import com.example.MutsaMarket.entity.NegotiationEntity;

public class UpdateProposalDto {
    private String writer;
    private String password;
    private Long suggestedPrice;
    private String status;

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(Long suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public String getStatus() {
        if (status == null)
            return "";
        else
            return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static UpdateProposalDto fromEntity(NegotiationEntity entity) {
        UpdateProposalDto dto = new UpdateProposalDto();
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        dto.setSuggestedPrice(entity.getSuggestedPrice());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
