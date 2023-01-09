package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.dto.user.UserRoleRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;

    //Login 기능 ------------------------------------

    @ApiOperation(value = "회원가입 기능",notes = "userName, password 입력해서 회원가입")
    @PostMapping(value = "/join" , consumes = "application/json")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest){
        UserJoinResponse userJoinResponse = userService.join(userJoinRequest);
        return Response.success(userJoinResponse);
    }

    @ApiOperation(value = "로그인 기능",notes = "회원가입한 정보로 로그인")
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        return Response.success(userLoginResponse);
    }

    //Admin 기능 ------------------------------------

    @ApiOperation(value = "권한 기능",notes = "ADMIN 계정이 사용자 권한 설정")
    @PostMapping("/{id}/role/change")
    public Response<String> changeRole(@RequestBody UserRoleRequest userRoleRequest, @PathVariable Long id,Authentication authentication){
        String role = userRoleRequest.getRole();
        String message =userService.userRoleChange(authentication.getName(),id,role);
        return Response.success(message);
    }

    //알림 기능 -------------------------------------
    @ApiOperation(value = "알람 조회 기능")
    @GetMapping("/alarm")
    public Response<?> alarm(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        Page<AlarmResponse> alarmResponsePage = userService.getAlarm(authentication.getName(),pageable);
        return Response.success(alarmResponsePage);
    }

}
