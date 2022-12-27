package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.user.UserRoleRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController  {
    private final UserService userService;

    //Login 기능 ------------------------------------

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        UserJoinResponse userJoinResponse =userService.join(userJoinRequest.getUserName(),userJoinRequest.getPassword());
        return Response.success(userJoinResponse);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return Response.success(userLoginResponse);
    }

    //Admin 기능 ------------------------------------
    @PostMapping("/{id}/role/change")
    public Response<String> changeRole(@RequestBody UserRoleRequest userRoleRequest, @PathVariable Long id,Authentication authentication){
        String userName = authentication.getName();
        String role = userRoleRequest.getRole();
        String message =userService.userRoleChange(userName,id,role);
        return Response.success(message);
    }

    //알림 기능 -------------------------------------



}
