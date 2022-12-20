package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.UserJoinRequest;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest userJoinRequest){
        userService.join(userJoinRequest.getUserName(),userJoinRequest.getPassword());
        return ResponseEntity.ok().body("가입완료");
    }
}
