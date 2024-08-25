package com.example.techeertree.service;

import com.example.techeertree.domain.Comment;
import com.example.techeertree.domain.Wish;
import com.example.techeertree.dto.comment.CommentMapper.*;
import com.example.techeertree.dto.comment.CommentRequestDto.*;
import com.example.techeertree.dto.comment.CommentResponseDto.*;
import com.example.techeertree.exception.BaseException;
import com.example.techeertree.exception.ErrorCode;
import com.example.techeertree.repository.CommentRepository;
import com.example.techeertree.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final WishRepository wishRepository;


    public CommentInfoResponseDto create(Long id, CommentCreateRequestDto commentCreateRequestDto) {
        // 소원 존재 유무 조회
        Wish wish = wishRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_WISH));

        // 댓글 객체 생성
        Comment comment = CommentCreateMapper.INSTANCE.toEntity(commentCreateRequestDto);
        comment.setWish(wish);
        commentRepository.save(comment);

        // 소원에 댓글 등록
        wish.getComments().add(comment);
        wishRepository.save(wish);

        return CommentCreateMapper.INSTANCE.toDto(comment);
    }
}
