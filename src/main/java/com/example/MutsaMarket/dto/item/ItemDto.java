package com.example.MutsaMarket.dto.item;

import com.example.MutsaMarket.entity.SalesItemEntity;
import lombok.Data;

@Data
public class ItemDto {
    private String title;
    private String username;
    private String description;
    private Long minPriceWanted;
    private String status;

    public static ItemDto fromEntity(SalesItemEntity entity) {
        ItemDto dto = new ItemDto();
        dto.setTitle(entity.getTitle());
        dto.setUsername(entity.getUser().getUsername());
        dto.setDescription(entity.getDescription());
        dto.setMinPriceWanted(entity.getMinPriceWanted());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
