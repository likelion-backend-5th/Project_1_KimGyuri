package com.example.MutsaMarket.controller;

import com.example.MutsaMarket.dto.comment.CommentDto;
import com.example.MutsaMarket.dto.comment.CommentListDto;
import com.example.MutsaMarket.dto.comment.ReplyDto;
import com.example.MutsaMarket.service.CommentService;
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
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    //댓글 작성
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@PathVariable("itemId") Long itemId, @Valid @RequestBody CommentDto dto) {
        log.info(dto.toString());
        service.createComment(itemId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 등록되었습니다.");

        return ResponseEntity.ok(responseBody);
    }

    //게시글 댓글 조회
    @GetMapping
    public Page<CommentListDto> readAll(@PathVariable("itemId") Long itemId) {
        return service.readCommentAll(itemId);
    }

    //게시글 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> update(@PathVariable("commentId") Long commentId, @PathVariable("itemId") Long itemId, @RequestBody CommentDto dto) {
        log.info(dto.toString());
        service.updateComment(itemId, commentId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글이 수정되었습니다.");

        return ResponseEntity.ok(responseBody);
    }

    //게시글 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("commentId") Long commentId, @PathVariable("itemId") Long itemId) {
        service.deleteComment(commentId, itemId);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글을 삭제했습니다.");

        return ResponseEntity.ok(responseBody);
    }

    //답글 작성
    @PutMapping("/{commentId}/reply")
    public ResponseEntity<Map<String, String>> reply(@PathVariable("commentId") Long commentId, @PathVariable("itemId") Long itemId, @RequestBody ReplyDto dto) {
        log.info(dto.toString());
        service.replyComment(itemId, commentId, dto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "댓글에 답변이 추가되었습니다.");

        return ResponseEntity.ok(responseBody);
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
