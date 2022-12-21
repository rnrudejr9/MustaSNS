package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/join")
    public ResponseEntity<Response> join(@RequestBody UserJoinRequest userJoinRequest){
        Response response =userService.join(userJoinRequest.getUserName(),userJoinRequest.getPassword());
        return ResponseEntity.ok().body(Response.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest){

        Response response = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return ResponseEntity.ok().body(new UserLoginResponse(response.getResult().toString()));
        //return ResponseEntity.ok().body(Response.success(response));
    }
}
