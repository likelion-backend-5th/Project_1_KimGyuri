package com.example.MutsaMarket.dto.item;

import com.example.MutsaMarket.entity.SalesItemEntity;
import lombok.Data;

@Data
public class ItemListDto {
    private Long id;
    private String title;
    private String description;
    private Long minPriceWanted;
    private String imageUrl;
    private String status;

    public static ItemListDto fromEntity(SalesItemEntity entity) {
        ItemListDto dto = new ItemListDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setImageUrl(entity.getImageUrl());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
