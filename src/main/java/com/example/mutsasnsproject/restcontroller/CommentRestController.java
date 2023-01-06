package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CommentRestController {
    private final CommentService commentService;
    //      댓글 crud ----------------------------------------------------

    @ApiOperation(value = "댓글 작성기능")
    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> addComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = commentService.commentAdd(userName, postId, commentRequest);
        return Response.success(commentResponse);
    }

    @ApiOperation(value = "댓글 조회기능")
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentListResponse>> listComment(@PathVariable Long postId, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentListResponse> commentListResponse = commentService.commentList(postId, pageable);
        return Response.success(commentListResponse);
    }

    @ApiOperation(value = "댓글 수정기능")
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> modifyComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = commentService.commentModify(userName, postId, commentRequest, id);
        return Response.success(commentResponse);
    }

    @ApiOperation(value = "댓글 삭제기능")
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> modifyComment2(Authentication authentication, @PathVariable Long postId, @PathVariable Long id) {
        String userName = authentication.getName();
        CommentResponse commentResponse = commentService.commentDelete(userName, postId, id);
        return Response.success(commentResponse);
    }
}
