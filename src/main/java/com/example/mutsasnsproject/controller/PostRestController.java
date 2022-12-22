package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostService postService;
    @PostMapping
    public ResponseEntity<Response> post(Authentication authentication,@RequestBody PostRequest postRequest){
        Response response = postService.createPost(postRequest.getBody(),postRequest.getTitle());
        return ResponseEntity.ok().body(response);
    }
}
