package com.example.MutsaMarket.exceptions;

import com.example.MutsaMarket.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(Status400Exception.class)
    public ResponseEntity<ResponseDto> handle400(Status400Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Status403Exception.class)
    public ResponseEntity<ResponseDto> handle403(Status403Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Status404Exception.class)
    public ResponseEntity<ResponseDto> handle404(Status404Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Status500Exception.class)
    public ResponseEntity<ResponseDto> handle500(Status500Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
