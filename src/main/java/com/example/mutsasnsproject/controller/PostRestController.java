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


    @PostMapping
    public ResponseEntity<Response> addPost(Authentication authentication,@RequestBody PostRequest postRequest){
        log.info("게시글 작성 컨트롤러");
        String userName = authentication.getName();
        log.info("userName = " + userName);
        PostResponse postResponse = postService.add(userName,postRequest.getBody(),postRequest.getTitle());
        return ResponseEntity.ok().body(Response.success(postResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> modifyPost(Authentication authentication, @RequestBody PostRequest postRequest,@PathVariable Long id){
        log.info("게시글 수정 컨트롤러");
        String userName = authentication.getName();
        PostResponse postResponse =postService.modify(userName,id, postRequest);
        return ResponseEntity.ok().body(Response.success(postResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(Authentication authentication,@PathVariable Long id){
        log.info("게시글 삭제 컨틀로러");
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName,id);
        return ResponseEntity.ok().body(Response.success(postResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPost(Authentication authentication,@PathVariable Long id){
        String userName = authentication.getName();
        log.info("1");
        PostDetailResponse postDetailResponse = postService.get(userName,id);
        log.info("2");
        return ResponseEntity.ok().body(Response.success(postDetailResponse));
    }
    @GetMapping
    public Response<Page<PostDetailResponse>> listPost(@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostDetailResponse> postListResponses = postService.list(pageable);
        return Response.success(postListResponses);
    }


    @PostMapping("/{id}/comment")
    public ResponseEntity<Response> addComment(Authentication authentication, @PathVariable Long id, @RequestBody CommentRequest commentRequest){
        String userName = authentication.getName();
        CommentResponse commentResponse = postService.commentAdd(userName,id, commentRequest.getComment());
        return ResponseEntity.ok().body(Response.success(commentResponse));
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<Response> listComment(@PathVariable Long id,@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        CommentListResponse commentListResponse = postService.commentList(id,pageable);
        return ResponseEntity.ok().body(Response.success(commentListResponse));
    }

    @PutMapping("/{postId}/comment/{id}")
    public ResponseEntity<Response> modifyComment(Authentication authentication,@PathVariable Long postId, @PathVariable Long id,@RequestBody CommentRequest commentRequest){
        String userName = authentication.getName();
        CommentResponse commentResponse =postService.commentModify(userName,postId,commentRequest,id);
        return ResponseEntity.ok().body(Response.success(commentResponse));
    }




}
