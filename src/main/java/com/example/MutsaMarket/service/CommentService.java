package com.example.MutsaMarket.service;

import com.example.MutsaMarket.dto.comment.CommentDto;
import com.example.MutsaMarket.dto.comment.CommentListDto;
import com.example.MutsaMarket.dto.comment.ReplyDto;
import com.example.MutsaMarket.entity.CommentEntity;
import com.example.MutsaMarket.entity.SalesItemEntity;
import com.example.MutsaMarket.entity.user.UserEntity;
import com.example.MutsaMarket.exceptions.AuthorizationException;
import com.example.MutsaMarket.exceptions.CommentNotFoundException;
import com.example.MutsaMarket.exceptions.ItemNotFoundException;
import com.example.MutsaMarket.jwt.JwtTokenUtils;
import com.example.MutsaMarket.repository.CommentRepository;
import com.example.MutsaMarket.repository.SalesItemRepository;
import com.example.MutsaMarket.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;
    private final HttpServletRequest request;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    //댓글 작성
    public CommentDto createComment(Long itemId, CommentDto dto) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //사용자를 찾지 못했습니다
                }
                UserEntity user = optionalUser.get();

                Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
                if (optionalSalesItem.isEmpty())
                    throw new ItemNotFoundException();

                CommentEntity newComment = new CommentEntity();
                newComment.setContent(dto.getContent());
                newComment.setSalesItem(optionalSalesItem.get());
                newComment.setUser(user);
                newComment = commentRepository.save(newComment);
                return CommentDto.fromEntity(newComment);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
        }
    }

    //게시글 댓글 조회
    public Page<CommentListDto> readCommentAll(Long itemId) {
        if (!salesItemRepository.existsById(itemId))
            throw new ItemNotFoundException();

        Pageable pageable = PageRequest.of(0, 25, Sort.by("id"));
        Page<CommentEntity> commentEntityPage = commentRepository.findAll(pageable);
        Page<CommentListDto> commentListDtoPage = commentEntityPage.map(CommentListDto::fromEntity);
        return commentListDtoPage;
    }

    //게시글 댓글 수정
    public CommentDto updateComment(Long itemId, Long commentId, CommentDto dto) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //사용자를 찾지 못했습니다
                }
                UserEntity user = optionalUser.get();

                Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
                if (optionalComment.isEmpty())
                    throw new CommentNotFoundException();

                CommentEntity comment = optionalComment.get();
                if (!itemId.equals(comment.getSalesItem().getId()))
                    throw new ItemNotFoundException();

                if (comment.getUser().getId().equals(user.getId())) {
                    comment.setContent(dto.getContent());
                    commentRepository.save(comment);
                    return CommentDto.fromEntity(comment);
                } else {
                    throw new AuthorizationException();
                }
            } else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
    }

    //게시글 댓글 삭제
    public void deleteComment(Long commentId, Long itemId) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //사용자를 찾지 못했습니다
                }
                UserEntity user = optionalUser.get();

                Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
                if (optionalComment.isEmpty())
                    throw new CommentNotFoundException();

                CommentEntity comment = optionalComment.get();
                if (!itemId.equals(comment.getSalesItem().getId()))
                    throw new ItemNotFoundException();

                if (comment.getUser().getId().equals(user.getId()))
                    commentRepository.deleteById(commentId);
                else
                    throw new AuthorizationException();
            } else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
    }

    //답글 작성
    public ReplyDto replyComment(Long itemId, Long commentId, ReplyDto dto) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenUtils.validate(token)) {
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //사용자를 찾지 못했습니다
                }
                UserEntity user = optionalUser.get();

                Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
                Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
                if (optionalSalesItem.isEmpty() || optionalComment.isEmpty())
                    throw new CommentNotFoundException();

                SalesItemEntity item = optionalSalesItem.get();
                CommentEntity comment = optionalComment.get();
                if (!itemId.equals(comment.getSalesItem().getId()))
                    throw new ItemNotFoundException();

                if (item.getUser().getId().equals(user.getId())) {
                    comment.setReply(dto.getReply());
                    commentRepository.save(comment);
                    return ReplyDto.fromEntity(comment);
                } else {
                    throw new AuthorizationException();
                }
            } else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //유효하지 않은 토큰입니다
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //토큰의 형식이 잘못되었습니다
    }
}

