package com.example.mutsasnsproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@Slf4j
public class UserController {
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("hello");
    }

}
