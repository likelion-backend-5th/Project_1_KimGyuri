package com.example.MutsaMarket.service;

import com.example.MutsaMarket.dto.comment.CommentDto;
import com.example.MutsaMarket.dto.comment.CommentListDto;
import com.example.MutsaMarket.dto.comment.ReplyDto;
import com.example.MutsaMarket.entity.CommentEntity;
import com.example.MutsaMarket.entity.SalesItemEntity;
import com.example.MutsaMarket.exceptions.AuthorizationException;
import com.example.MutsaMarket.exceptions.CommentNotFoundException;
import com.example.MutsaMarket.exceptions.ItemNotFoundException;
import com.example.MutsaMarket.repository.CommentRepository;
import com.example.MutsaMarket.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final SalesItemRepository salesItemRepository;
    private final CommentRepository commentRepository;

    //댓글 작성
    public CommentDto createComment(Long itemId, CommentDto dto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ItemNotFoundException();

        CommentEntity newComment = new CommentEntity();
        newComment.setWriter(dto.getWriter());
        newComment.setPassword(dto.getPassword());
        newComment.setContent(dto.getContent());
        newComment.setItemId(itemId);
        newComment = commentRepository.save(newComment);
        return CommentDto.fromEntity(newComment);
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
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new CommentNotFoundException();

        CommentEntity comment = optionalComment.get();
        if (!itemId.equals(comment.getItemId()))
            throw new ItemNotFoundException();
        if (comment.getWriter().equals(dto.getWriter()) && comment.getPassword().equals(dto.getPassword())) {
            comment.setContent(dto.getContent());
            commentRepository.save(comment);
            return CommentDto.fromEntity(comment);
        } else {
            throw new AuthorizationException();
        }
    }

    //게시글 댓글 삭제
    public void deleteComment(Long commentId, Long itemId, CommentDto dto) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
            throw new CommentNotFoundException();

        CommentEntity comment = optionalComment.get();
        if(!itemId.equals(comment.getItemId()))
            throw new ItemNotFoundException();

        if(comment.getWriter().equals(dto.getWriter()) && comment.getPassword().equals(dto.getPassword()))
            commentRepository.deleteById(commentId);
        else
            throw new AuthorizationException();
    }

    //답글 작성
    public ReplyDto replyComment(Long itemId, Long commentId, ReplyDto dto) {
        Optional<SalesItemEntity> optionalSalesItem = salesItemRepository.findById(itemId);
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalSalesItem.isEmpty() || optionalComment.isEmpty())
            throw new CommentNotFoundException();

        SalesItemEntity item = optionalSalesItem.get();
        CommentEntity comment = optionalComment.get();
        if (!itemId.equals(comment.getItemId()))
            throw new ItemNotFoundException();
        if (item.getWriter().equals(dto.getWriter()) && item.getPassword().equals(dto.getPassword())) {
            comment.setReply(dto.getReply());
            commentRepository.save(comment);
            return ReplyDto.fromEntity(comment);
        } else {
            throw new AuthorizationException();
        }
    }
}

