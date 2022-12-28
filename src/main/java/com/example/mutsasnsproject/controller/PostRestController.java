package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostListResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.PostService;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostRestController {
    private final PostService postService;

//    게시글 crud ----------------------------------------------------

    @PostMapping
    public Response<PostResponse> addPost(Authentication authentication,@RequestBody PostRequest postRequest){
        log.info("게시글 작성 컨트롤러");
        String userName = authentication.getName();
        PostResponse postResponse = postService.add(userName,postRequest.getBody(),postRequest.getTitle());
        return Response.success(postResponse);
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modifyPost(Authentication authentication, @RequestBody PostRequest postRequest,@PathVariable Long postId){
        log.info("게시글 수정 컨트롤러");
        String userName = authentication.getName();
        PostResponse postResponse =postService.modify(userName,postId, postRequest);
        return Response.success(postResponse);
    }

    @DeleteMapping("/{postId}")
    public Response<PostResponse> deletePost(Authentication authentication,@PathVariable Long postId){
        log.info("게시글 삭제 컨트롤러");
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName,postId);
        return Response.success(postResponse);
    }

    @GetMapping("/{postId}")
    public Response<PostDetailResponse> getPost(Authentication authentication,@PathVariable Long postId){
        String userName = authentication.getName();
        PostDetailResponse postDetailResponse = postService.get(userName,postId);
        return Response.success(postDetailResponse);
    }
    @GetMapping
    public Response<Page<PostDetailResponse>> listPost(@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostDetailResponse> postListResponses = postService.list(pageable);
        return Response.success(postListResponses);
    }

//      댓글 crud ----------------------------------------------------

    @PostMapping("/{postId}/comment")
    public Response<CommentResponse> addComment(Authentication authentication, @PathVariable Long postId, @RequestBody CommentRequest commentRequest){
        String userName = authentication.getName();
        CommentResponse commentResponse = postService.commentAdd(userName,postId, commentRequest.getComment());
        return Response.success(commentResponse);
    }

    @GetMapping("/{postId}/comment")
    public Response<CommentListResponse> listComment(@PathVariable Long postId,@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        CommentListResponse commentListResponse = postService.commentList(postId,pageable);
        return Response.success(commentListResponse);
    }

    @PutMapping("/{postId}/comment/{id}")
    public Response<CommentResponse> modifyComment(Authentication authentication,@PathVariable Long postId, @PathVariable Long id,@RequestBody CommentRequest commentRequest){
        String userName = authentication.getName();
        CommentResponse commentResponse =postService.commentModify(userName,postId,commentRequest,id);
        return Response.success(commentResponse);
    }

    @DeleteMapping("/{postId}/comment/{id}")
    public Response<CommentResponse> modifyComment2(Authentication authentication,@PathVariable Long postId, @PathVariable Long id){
        String userName = authentication.getName();
        CommentResponse commentResponse =postService.commentDelete(userName,postId,id);
        return Response.success(commentResponse);
    }

//    좋아요기능 ----------------------------------------------------

    @PostMapping("/{postId}/likes")
    public Response<String> goodPost(@PathVariable Long postId,Authentication authentication){
        log.info("게시글 좋아요 컨트롤러");
        String userName = authentication.getName();
        String message = postService.postGood(postId,userName);
        return Response.success(message);
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> goodCount(@PathVariable long postId){
        int count = postService.countGood(postId);
        return Response.success(count);
    }



}
