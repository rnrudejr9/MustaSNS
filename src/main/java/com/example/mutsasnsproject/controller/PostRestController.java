package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
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
        loginCheck(authentication);
        String userName = authentication.getName();
        PostResponse postResponse = postService.createPost(userName,postRequest.getBody(),postRequest.getTitle());
        return ResponseEntity.ok().body(Response.success(postResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> modifyPost(Authentication authentication, @RequestBody PostRequest postRequest,@PathVariable Long id){
        log.info("게시글 수정 컨트롤러");
        loginCheck(authentication);
        String userName = authentication.getName();
        PostResponse postResponse =postService.modifyPost(userName,id, postRequest);
        return ResponseEntity.ok().body(Response.success(postResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(Authentication authentication,@PathVariable Long id){
        log.info("게시글 삭제 컨틀로러");
        loginCheck(authentication);
        String userName = authentication.getName();
        PostResponse postResponse = postService.delete(userName,id);
        return ResponseEntity.ok().body(Response.success(postResponse));
    }


    @GetMapping
    public ResponseEntity<Response> getPost(@PageableDefault(size = 20, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        PostListResponse postListResponse = postService.list(pageable);
        return ResponseEntity.ok().body(Response.success(postListResponse));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> detailPost(Authentication authentication,@PathVariable Long id){
        loginCheck(authentication);
        PostDetailResponse postDetailResponse = postService.detail(id);
        return ResponseEntity.ok().body(Response.success(postDetailResponse));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Response> likePost(Authentication authentication,@PathVariable Long id){
        loginCheck(authentication);
        return null;
    }

    public void loginCheck(Authentication authentication){
        if(authentication == null){
            throw new AppException(ErrorCode.INVALID_PERMISSION, "로그인을 해주세요!");
        }
    }

}
