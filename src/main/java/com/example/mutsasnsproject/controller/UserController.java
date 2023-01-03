package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/")
public class UserController {
    UserService userService;

    @GetMapping("/users/login")
    public String login(){
       return "login";
    }
    @PostMapping("/users/login")
    public String login(@RequestBody UserLoginRequest userLoginRequest){
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest.getUserName(),userLoginRequest.getPassword());
        return "redirect:/";
    }

    @GetMapping("/users/join")
    public String join2(Model model){
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "users/join";
    }

    //modelAtrribute 로 데이터 값을 받음
    @PostMapping("/users/join")
    public String join(@ModelAttribute UserJoinRequest userJoinRequest, Model model){
        System.out.println(userJoinRequest.getUserName());
        try {
            userService.join(userJoinRequest.getUserName(), userJoinRequest.getPassword());
        } catch(AppException e) {
            if(e.getErrorCode() == ErrorCode.USERNAME_DUPLICATED) {
                model.addAttribute("message", "UserName이 중복됩니다");
                model.addAttribute("nextUrl", "/users/join");
                model.addAttribute("userJoinRequest", new UserJoinRequest());
                return "users/join";
            }
        } catch (Exception e) {
            throw e;
        }
        System.out.println(userJoinRequest.getUserName());
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        model.addAttribute("message", "회원가입이 완료 되었습니다\n로그인 해주세요");
        model.addAttribute("nextUrl", "/users/login");
        return "users/login";
    }
}
