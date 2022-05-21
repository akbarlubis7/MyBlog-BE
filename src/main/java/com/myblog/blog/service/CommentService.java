package com.myblog.blog.service;

import com.myblog.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(CommentDto commentRequests, Long postId, Long id);

    void deleteComment(Long postId, Long id);
}
