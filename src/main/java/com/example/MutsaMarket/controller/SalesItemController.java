package com.example.MutsaMarket.controller;

import com.example.MutsaMarket.dto.ItemListDto;
import com.example.MutsaMarket.dto.SalesItemDto;
import com.example.MutsaMarket.service.SalesItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;

    //물품 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody SalesItemDto salesItemDto) {
        log.info(salesItemDto.toString());
        service.createItem(salesItemDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "등록이 완료되었습니다.");

        return ResponseEntity.ok(responseBody);
    }

    //물품 전체 조회
    @GetMapping
    public Page<ItemListDto> readAll(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return service.readItemAll(page, limit);
    }

    //유효성 검증 결과 오류
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error: exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
}