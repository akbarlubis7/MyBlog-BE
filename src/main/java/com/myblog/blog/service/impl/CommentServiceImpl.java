package com.myblog.blog.service.impl;

import com.myblog.blog.dto.CommentDto;
import com.myblog.blog.entity.Comment;
import com.myblog.blog.entity.Post;
import com.myblog.blog.exception.BlogApiException;
import com.myblog.blog.exception.ResourceNotFoundException;
import com.myblog.blog.repository.CommentRepository;
import com.myblog.blog.repository.PostRepository;
import com.myblog.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository  commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        //set post to comment entity
        comment.setPost(post);

        //save comment entity to database
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        //retrieve comment by post id
        List<Comment> comments = commentRepository.findByPostId(postId);

        //convert entity to dto
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Comment comment = getCommentinPost(postId, commentId);

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(CommentDto commentRequest, Long postId, Long id) {
        Comment comment = getCommentinPost(postId, id);

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long id) {
        Comment comment = getCommentinPost(postId, id);
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return comment;
    }

    private Comment getCommentinPost(Long postId, Long id) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id)
        );

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return comment;
    }
}
