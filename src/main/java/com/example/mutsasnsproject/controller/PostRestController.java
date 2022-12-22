package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.PostService;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostService postService;


    @PostMapping
    public ResponseEntity<Response> addPost(Authentication authentication,@RequestBody PostRequest postRequest){
        loginCheck(authentication);
        String userName = authentication.getName();
        Response response = postService.createPost(userName,postRequest.getBody(),postRequest.getTitle());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> modifyPost(Authentication authentication, @RequestBody PostRequest postRequest,@PathVariable Long id){
        loginCheck(authentication);
        String userName = authentication.getName();
        Response response =postService.modifyPost(userName,id, postRequest);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePost(Authentication authentication,@PathVariable Long id){
        loginCheck(authentication);
        String userName = authentication.getName();
        Response response = postService.delete(userName,id);
        return ResponseEntity.ok().body(response);
    }


//    @GetMapping
//    public ResponseEntity<Response> getPost(){
//
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Response> detailPost(){
//
//    }

    public void loginCheck(Authentication authentication){
        if(authentication == null){
            throw new AppException(ErrorCode.INVALID_PERMISSION, "로그인을 해주세요!");
        }
    }

}
