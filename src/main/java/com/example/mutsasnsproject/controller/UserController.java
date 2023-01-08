package com.example.mutsasnsproject.controller;


import com.example.mutsasnsproject.domain.dto.user.UserJoinRequest;
import com.example.mutsasnsproject.domain.dto.user.UserJoinResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.domain.dto.user.UserLoginResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("userLoginRequest",new UserLoginRequest());
       return "users/login";
    }
    @PostMapping("/login")
    public String login(@Valid UserLoginRequest userLoginRequest , HttpServletRequest httpServletRequest, Model model) {
        String jwtToken = "";
        try {
            jwtToken = userService.login(userLoginRequest).getJwt();
        }catch (AppException e){
            model.addAttribute("error_message",e.getErrorCode().getMessage());
            return "users/login";
        }
        // 기존 Session 파기 후 새로 생성
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);

        // login 후 받은 jwt Token 값을 session에 넣어줌
        session.setAttribute("jwt", "Bearer " + jwtToken);
        session.setMaxInactiveInterval(1800); // Session이 30분동안 유지

        return "redirect:/view/v1/posts/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("jwt");
        session.invalidate();
        return "redirect:/view/v1/home";
    }


    @GetMapping("/join")
    public String join2(Model model){
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "users/join";
    }

    //modelAtrribute 로 데이터 값을 받음
    @PostMapping("/join")
    public String join(@ModelAttribute UserJoinRequest userJoinRequest, Model model){
        System.out.println(userJoinRequest.getUserName());
        try {
            userService.join(userJoinRequest);
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
