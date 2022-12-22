package com.example.mutsasnsproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @PostMapping
    public ResponseEntity<String> post(Authentication authentication){
        return ResponseEntity.ok().body(authentication.getName() + " 포스트작성");
    }
}
