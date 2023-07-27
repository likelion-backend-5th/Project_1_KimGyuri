package com.example.MutsaMarket.service;

import com.example.MutsaMarket.dto.item.ItemDto;
import com.example.MutsaMarket.dto.item.ItemListDto;
import com.example.MutsaMarket.dto.item.SalesItemDto;
import com.example.MutsaMarket.entity.SalesItemEntity;
import com.example.MutsaMarket.exceptions.AuthorizationException;
import com.example.MutsaMarket.exceptions.ImageUploadException;
import com.example.MutsaMarket.exceptions.ItemNotFoundException;
import com.example.MutsaMarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesItemService {
    private final SalesItemRepository repository;

    //물품 등록
    public SalesItemDto createItem(SalesItemDto dto) {
        SalesItemEntity newItem = new SalesItemEntity();
        newItem.setTitle(dto.getTitle());
        newItem.setDescription(dto.getDescription());
        newItem.setMinPriceWanted(dto.getMinPriceWanted());
        newItem.setWriter(dto.getWriter());
        newItem.setPassword(dto.getPassword());
        newItem.setStatus("판매중");
        return SalesItemDto.fromEntity(this.repository.save(newItem));
    }

    //물품 전체 조회
    public Page<ItemListDto> readItemAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        Page<SalesItemEntity> itemEntityPage = repository.findAll(pageable);
        Page<ItemListDto> itemListDtoPage = itemEntityPage.map(ItemListDto::fromEntity);
        return itemListDtoPage;
    }

    //물품 단일 조회
    public ItemDto readItem(Long id) {
        Optional<SalesItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent())
            return ItemDto.fromEntity(optionalItem.get());
        else
            throw new ItemNotFoundException();
    }

    //물품 정보 수정
    public SalesItemDto updateItem(Long id, SalesItemDto dto) {
        Optional<SalesItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isPresent()) {
            SalesItemEntity item = optionalItem.get();
            if (item.getWriter().equals(dto.getWriter()) && item.getPassword().equals(dto.getPassword())) {
                item.setTitle(dto.getTitle());
                item.setDescription(dto.getDescription());
                item.setMinPriceWanted(dto.getMinPriceWanted());
                repository.save(item);
                return SalesItemDto.fromEntity(item);
            } else
                throw new AuthorizationException();
        } else
            throw new ItemNotFoundException();
    }

    //물품 이미지 등록
    public ItemListDto updateImage(Long id, MultipartFile image, String writer, String password) {
        Optional<SalesItemEntity> optionalSalesItem = repository.findById(id);
        if (optionalSalesItem.isEmpty())
            throw new ItemNotFoundException();

        SalesItemEntity item = optionalSalesItem.get();
        if (item.getWriter().equals(writer) && item.getPassword().equals(password)) {
            String profileDir = String.format("media/%d/", id);
            try {
                Files.createDirectories(Path.of(profileDir));
            } catch (IOException e) {
                throw new ImageUploadException();
            }

            String originalFilename = image.getOriginalFilename();
            String[] fileNameSplit = originalFilename.split("\\."); //정규표현식을 기준으로 split
            String extension = fileNameSplit[fileNameSplit.length-1]; //split 제일 마지막이 확장자
            String profileFilename = "image." + extension;

            String profilePath = profileDir + profileFilename;

            try {
                image.transferTo(Path.of(profilePath));
            } catch (IOException e) {
                throw new ImageUploadException();
            }
            SalesItemEntity salesItem = optionalSalesItem.get();
            salesItem.setImageUrl(String.format("/static/%d/%s", id, profileFilename));
            return ItemListDto.fromEntity(repository.save(salesItem));
        } else
            throw new AuthorizationException();
    }

    //물품 정보 삭제
    public void deleteItem(Long id, SalesItemDto dto) {
        Optional<SalesItemEntity> optionalSalesItem = repository.findById(id);
        if (optionalSalesItem.isEmpty())
            throw new ItemNotFoundException();

        SalesItemEntity item = optionalSalesItem.get();
        if (item.getWriter().equals(dto.getWriter()) && item.getPassword().equals(dto.getPassword())) {
            repository.deleteById(id);
        } else
            throw new AuthorizationException();
    }
}
