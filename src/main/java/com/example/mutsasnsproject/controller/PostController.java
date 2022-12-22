package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/posts")
public class PostController {
    @GetMapping("/misssss")
    public ResponseEntity<Response> getPost(){
        return null;
    }
}
