package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {
    private final PostService postService;

//      게시글 crud ----------------------------------------------------
    @ApiOperation(value = "게시글 작성기능", notes = "로그인 한 사용자가 title,body 값으로 게시글 작성")
    @PostMapping
    public Response<PostResponse> addPost(@ApiIgnore Authentication authentication, @RequestBody PostRequest postRequest) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.add(userName, postRequest.getBody(), postRequest.getTitle());
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 수정기능", notes = "로그인 한 사용자가 title,body 값으로 게시글 수정")
    @PutMapping("/{id}")
    public Response<PostResponse> modifyPost(@ApiIgnore Authentication authentication, @RequestBody PostRequest postRequest, @PathVariable Long id) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.modify(userName, id, postRequest);
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 삭제기능")
    @DeleteMapping("/{id}")
    public Response<PostResponse> deletePost(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName, id);
        return Response.success(postResponse);
    }

    @ApiOperation(value = "게시글 상세조회기능")
    @GetMapping("/{id}")
    public Response<PostDetailResponse> getPost(@ApiIgnore Authentication authentication, @PathVariable Long id) {
        String userName = authentication.getName();
        PostDetailResponse postDetailResponse = postService.get(userName, id);
        return Response.success(postDetailResponse);
    }

    @ApiOperation(value = "게시글 전체조회기능")
    @GetMapping
    public Response<Page<PostDetailResponse>> listPost(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDetailResponse> postListResponses = postService.list(pageable);
        return Response.success(postListResponses);
    }

//      댓글 crud ----------------------------------------------------

    @ApiOperation(value = "댓글 작성기능")
    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> addComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = postService.commentAdd(userName, postId, commentRequest.getComment());
        return Response.success(commentResponse);
    }

    @ApiOperation(value = "댓글 조회기능")
    @GetMapping("/{postId}/comments")
    public Response<CommentListResponse> listComment(@PathVariable Long postId, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        CommentListResponse commentListResponse = postService.commentList(postId, pageable);
        return Response.success(commentListResponse);
    }

    @ApiOperation(value = "댓글 수정기능")
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> modifyComment(Authentication authentication, @PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        String userName = authentication.getName();
        CommentResponse commentResponse = postService.commentModify(userName, postId, commentRequest, id);
        return Response.success(commentResponse);
    }

    @ApiOperation(value = "댓글 삭제기능")
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> modifyComment2(Authentication authentication, @PathVariable Long postId, @PathVariable Long id) {
        String userName = authentication.getName();
        CommentResponse commentResponse = postService.commentDelete(userName, postId, id);
        return Response.success(commentResponse);
    }

//    좋아요기능 ----------------------------------------------------

    @ApiOperation(value = "좋아요 실행/취소기능")
    @PostMapping("/{postId}/likes")
    public Response<String> goodPost(@PathVariable Long postId, Authentication authentication) {
        String userName = authentication.getName();
        String message = postService.postGood(postId, userName);
        return Response.success(message);
    }

    @ApiOperation(value = "좋아요 조회기능")
    @GetMapping("/{postId}/likes")
    public Response<Integer> goodCount(@PathVariable long postId) {
        int count = postService.countGood(postId);
        return Response.success(count);
    }

//    마이 피드 기능 ------------------------------------------------------

    @ApiOperation(value = "마이피드 기능")
    @GetMapping("/my")
    public Response<?> myPage(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page postDetailResponse = postService.myPages(userName, pageable);
        return Response.success(postDetailResponse);
    }


}
