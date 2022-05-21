package com.myblog.blog.controller;

import com.myblog.blog.dto.CommentDto;
import com.myblog.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") Long postId,
                                                    @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                            @PathVariable(value = "id") Long id) {
        CommentDto commentDto = commentService.getCommentById(postId, id);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentRequest,
                                                    @PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "id") Long id) {
        CommentDto commentDtoResponse = commentService.updateComment(commentRequest, postId, id);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long id) {
        commentService.deleteComment(postId, id);

        return new ResponseEntity<>("Comment successfully deleted", HttpStatus.OK);
    }

}
